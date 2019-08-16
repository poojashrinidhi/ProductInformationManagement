package com.commercetools.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.commercetools.models.Product;
import com.commercetools.models.ProductStatisticsResponseDTO;
import com.commercetools.service.dao.ProductServiceDao;

/**
 * Rest endpoint for accessing Products
 * 
 * @author PoojaShankar
 */

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	@Qualifier("productService")
	private ProductServiceDao productService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Product> getAllProducts() {
		return productService.getAllProducts();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/statistics")
	public ProductStatisticsResponseDTO getAllProductStatistics() {
		return productService.getStatisticsOfCreatedAndModifiedProducts();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public Product updateProduct(@RequestBody Product user, @PathVariable UUID id) {
		return productService.updateProduct(id, user);
	}

	@RequestMapping(method = RequestMethod.POST)
	public Product addUProduct(@RequestBody Product user) {
		return productService.addProduct(user);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deletProduct(@PathVariable UUID id) {
		productService.deletProduct(id);
	}
}
