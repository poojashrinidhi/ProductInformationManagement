package com.commercetools.service.dao;

import java.util.List;
import java.util.UUID;

import com.commercetools.models.Product;
import com.commercetools.models.ProductStatisticsResponseDTO;

/**
 * Product Service to interact with Database Repository
 * 
 * @author PoojaShankar
 */
public interface ProductServiceDao {

	List<Product> getAllProducts();

	Product getProductByUuid(UUID id);

	Product addProduct(Product user);

	Product updateProduct(UUID id, Product product);

	void deletProduct(UUID id);

	List<Product> getAllProductsCreatedToday();

	List<Product> getAllProductsModifiedToday();

	ProductStatisticsResponseDTO getStatisticsOfCreatedAndModifiedProducts();

}
