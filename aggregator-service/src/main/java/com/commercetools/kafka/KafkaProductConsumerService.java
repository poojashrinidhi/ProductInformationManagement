package com.commercetools.kafka;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.commercetools.models.Product;

/**
 * Placeholder Service for Kafka Consumer implementation
 * 
 * @author PoojaShankar
 */
public interface KafkaProductConsumerService {

	void receive(@Payload Product data, @Headers MessageHeaders headers);

}
