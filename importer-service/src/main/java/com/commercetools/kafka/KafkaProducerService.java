package com.commercetools.kafka;

import org.apache.kafka.common.errors.InvalidTopicException;

import com.commercetools.models.Product;

/**
 * Placeholder Service for Kafka producer implementation
 * 
 * @author PoojaShankar
 */

public interface KafkaProducerService {

	void send(Product data) throws InvalidTopicException;

}