package com.commercetools.service.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commercetools.models.Product;
import com.commercetools.models.ProductQueryResponseDTO;
import com.commercetools.models.ProductStatisticsResponseDTO;
import com.commercetools.models.Statistics;
import com.commercetools.repository.ProductRepository;
import com.commercetools.service.dao.ProductServiceDao;

/**
 * Product Service implementation to interact with Database Repository with
 * locks
 * 
 * @author PoojaShankar
 */

@Service("productService")
public class ProducerServiceDaoImpl implements ProductServiceDao {

	@Autowired
	private ProductRepository productRepository;

	// To handle concurrency operations
	private ReentrantLock lock = new ReentrantLock();

	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<Product>();
		productRepository.findAll().forEach(products::add);
		return products;
	}

	@Override
	public ProductStatisticsResponseDTO getStatisticsOfCreatedAndModifiedProducts() {

		// Get number of products created per day basis
		List<ProductQueryResponseDTO> createdProductsStatistics = productRepository
				.getTotalCountOfProductsCreatedPerDayBasis();

		// Get number of products modified per day basis
		List<ProductQueryResponseDTO> modifiedProductsStatistics = productRepository
				.getTotalCountOfProductsModifiedPerDayBasis();

		Map<String, Long> createdProductsMap = createdProductsStatistics.stream()
				.collect(Collectors.toMap(custom -> custom.getDate(), custom -> custom.getCount()));
		Map<String, Long> modifiedProductsMap = modifiedProductsStatistics.stream()
				.collect(Collectors.toMap(custom -> custom.getDate(), custom -> custom.getCount()));

		ProductStatisticsResponseDTO dto = new ProductStatisticsResponseDTO();
		Map<String, Statistics> productStatistics = new HashMap<String, Statistics>();
		dto.setProductStatistics(productStatistics);

		// For each entry in created map get modified count and created count
		for (String date : createdProductsMap.keySet()) {
			Statistics s = new Statistics();
			s.setNumberOfItemsCreated(createdProductsMap.getOrDefault(date, 0L));
			s.setNumberOfItemsModified(modifiedProductsMap.getOrDefault(date, 0L));
			productStatistics.put(date, s);
			modifiedProductsMap.remove(date);
		}

		// Add left entries from the modified map
		for (String date : modifiedProductsMap.keySet()) {
			Statistics s = new Statistics();
			s.setNumberOfItemsCreated(0L);
			s.setNumberOfItemsModified(modifiedProductsMap.getOrDefault(date, 0L));
			productStatistics.put(date, s);
		}

		return dto;
	}

	@Override
	public Product addProduct(Product product) {
		Optional<Product> existingProduct = productRepository.findById(product.getUuid());
		if (existingProduct.isPresent()) { // Already exists . Retain createdTimestamp. Update others.
			product.setCreatedTimeStamp(existingProduct.get().getCreatedTimeStamp());
			product.setLastModifiedTimeStamp(new Date());
		}
		lock.lock();
		Product item;
		try {
			item = productRepository.save(product);
		} finally {
			lock.unlock();
		}
		return item;
	}

	@Override
	public Product updateProduct(UUID id, Product product) {
		product.setUuid(id);
		product.setLastModifiedTimeStamp(new Date());
		lock.lock();
		Product item;
		try {
			item = productRepository.save(product);
		} finally {
			lock.unlock();
		}

		return item;

	}

	@Override
	public void deletProduct(UUID id) {
		lock.lock();
		try {
			productRepository.deleteById(id);
		} finally {
			lock.unlock();
		}
	}

	// Additional methods

	@Override
	public List<Product> getAllProductsCreatedToday() {
		List<Product> products = new ArrayList<Product>();
		productRepository.findAllByCreatedTimeStamp(new Date()).forEach(products::add);
		return products;
	}

	@Override
	public List<Product> getAllProductsModifiedToday() {
		List<Product> products = new ArrayList<Product>();
		productRepository.findAllByLastModifiedTimeStamp(new Date()).forEach(products::add);
		return products;
	}

	@Override
	public Product getProductByUuid(UUID id) {
		Optional<Product> product = productRepository.findById(id);
		return product.get();
	}
}
