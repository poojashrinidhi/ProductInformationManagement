package com.commercetools.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commercetools.kafka.KafkaProducerService;
import com.commercetools.models.Product;
import com.commercetools.service.ObjectConverterService;

/**
 * Converts the csv read data to the actual Product Format and Sends to Kafka
 * Queue
 * 
 * @author PoojaShankar
 */

@Service("productService")
public class ProductConverterServiceImpl implements ObjectConverterService {

	private static final String COMMA_DELIMITER = ",";

	@Autowired
	private KafkaProducerService kafkaProducerService;

	@Override
	public void formatAndSendToKafka(String line) {
		String[] values = line.split(COMMA_DELIMITER);
		Product product = createProductObject(values);
		kafkaProducerService.send(product);
	}

	private Product createProductObject(String[] values) {
		UUID uuid = UUID.fromString(values[0]);
		String name = values[1];
		String description = values[2];
		String provider = values[3];
		boolean available = Boolean.valueOf(values[4]);
		String measurementUnits = values[5];
		return new Product(uuid, name, description, provider, available, measurementUnits);

	}
}