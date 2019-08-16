package com.commercetools.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.commercetools.models.Product;
import com.commercetools.service.dao.ProductServiceDao;

import lombok.extern.slf4j.Slf4j;

/**
 * Receives the data from the Kafka Queue for topics.
 * 
 * @author PoojaShankar
 */

@Slf4j
@Service
public class KafkaProductConsumerServiceImpl implements KafkaProductConsumerService {

	@Autowired
	@Qualifier("productService")
	private ProductServiceDao productService;

	@Override
	@KafkaListener(topics = "${spring.kafka.topics}")
	public void receive(@Payload Product data, @Headers MessageHeaders headers) {

		log.info("received data='{}'", data);
		//Add to database
		productService.addProduct(data);
	}
}