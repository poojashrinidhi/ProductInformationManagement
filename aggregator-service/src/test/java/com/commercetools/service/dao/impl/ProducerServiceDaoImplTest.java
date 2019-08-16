package com.commercetools.service.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.commercetools.models.Product;
import com.commercetools.models.ProductStatisticsResponseDTO;
import com.commercetools.repository.ProductRepository;

/**
 * @author PoojaShankar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(topics = { "product" })
@TestPropertySource(properties = { "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
"spring.kafka.consumer.auto-offset-reset=latest"})
public class ProducerServiceDaoImplTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProducerServiceDaoImpl service;

	// Verify no SQL Constraint exception is received when two threads creates same
	// product at same time
	@Test
	public void testConcurrentAddProduct() throws InterruptedException {
		productRepository.deleteAll();
		Product product = new Product(UUID.randomUUID(), "sony", "tv", "sun", true, "pc");
		runCreateProductWithMultiThread(() -> {
			try {
				Thread.sleep(1000);
				service.addProduct(product);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 10);

		long count = productRepository.count();
		assertEquals(1, count);
	}

	@Test
	public void testGetStatisticsOfCreatedAndModifiedProducts() {
		productRepository.deleteAll();
		// Add 5 products to database
		String dateCreated = null;
		for (Product product : createProducts()) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateCreated = dateFormat.format(service.addProduct(product).getCreatedTimeStamp());
		}

		// Test-1 : Verify that statistics has 5 products created and 0 products
		// modified
		ProductStatisticsResponseDTO statistics = service.getStatisticsOfCreatedAndModifiedProducts();
		assertTrue(statistics.getProductStatistics().get(dateCreated).getNumberOfItemsCreated() == 5);
		assertTrue(statistics.getProductStatistics().get(dateCreated).getNumberOfItemsModified() == 0);

		// Test-2 : Verify that statistics has 5 products created and 1 products
		// modified
		Product product = productRepository.findByName("a");
		service.updateProduct(product.getUuid(), product);
		statistics = service.getStatisticsOfCreatedAndModifiedProducts();
		assertTrue(statistics.getProductStatistics().get(dateCreated).getNumberOfItemsModified() == 1);

		// Test-2 : Verify that statistics has 5 products created and 1 products
		// modified since modification is done on the same product
		service.updateProduct(product.getUuid(), product);
		statistics = service.getStatisticsOfCreatedAndModifiedProducts();
		assertTrue(statistics.getProductStatistics().get(dateCreated).getNumberOfItemsModified() == 1);

		// Test-2 : Verify that statistics has 5 products created and 2 products
		// modified
		product = productRepository.findByName("b");
		service.updateProduct(product.getUuid(), product);
		statistics = service.getStatisticsOfCreatedAndModifiedProducts();
		assertTrue(statistics.getProductStatistics().get(dateCreated).getNumberOfItemsModified() == 2);
	}

	@Test
	public void testGetStatisticsOfCreatedAndModifiedProductsWithDifferentTests() {
		productRepository.deleteAll();
		// Add 5 products to database
		List<Product> productList = createProducts();

		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// change 3 products to be of date - 2020-01-01

		cal.set(2020, 1, 1);
		Date date = cal.getTime();
		String dayBefore = dateFormat.format(date);
		for (int i = 0; i < 3; i++) {
			productList.get(i).setCreatedTimeStamp(date);
			productList.get(i).setLastModifiedTimeStamp(date);
			productRepository.save(productList.get(i));
		}

		// change 2 products to be of date - 2020-02-01
		cal.set(2020, 2, 1);
		date = cal.getTime();
		String dayAfter = dateFormat.format(date);

		for (int i = 3; i < 5; i++) {
			productList.get(i).setCreatedTimeStamp(date);
			productList.get(i).setLastModifiedTimeStamp(date);
			productRepository.save(productList.get(i));
		}

		// Test-1 : Verify that statistics has 3 products created for date 2020-01-01
		// and 2 products created for 2020-02-01
		ProductStatisticsResponseDTO statistics = service.getStatisticsOfCreatedAndModifiedProducts();
		assertTrue(statistics.getProductStatistics().get(dayBefore).getNumberOfItemsCreated() == 3);
		assertTrue(statistics.getProductStatistics().get(dayAfter).getNumberOfItemsCreated() == 2);
	}

	public static void runCreateProductWithMultiThread(Runnable runnable, int noOfCounts) throws InterruptedException {

		ExecutorService es = Executors.newFixedThreadPool(2);
		for (int i = 0; i < noOfCounts; i++) {
			es.execute(runnable);
		}
		es.shutdown();
		es.awaitTermination(1, TimeUnit.MINUTES);
	}

	private static List<Product> createProducts() {

		List<Product> products = new ArrayList<Product>();
		products.add(new Product(UUID.randomUUID(), "a", "tv", "sony", true, "pc"));
		products.add(new Product(UUID.randomUUID(), "b", "tv", "sony", true, "pc"));
		products.add(new Product(UUID.randomUUID(), "c", "tv", "sony", true, "pc"));
		products.add(new Product(UUID.randomUUID(), "d", "tv", "sony", true, "pc"));
		products.add(new Product(UUID.randomUUID(), "e", "tv", "sony", true, "pc"));
		return products;
	}
}
