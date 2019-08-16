package com.commercetools.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.commercetools.models.Product;

import lombok.extern.slf4j.Slf4j;

/**
 * Sends the data to the Kafka Queue using kafka template.
 * 
 * @author PoojaShankar
 */

@Slf4j
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

	@Value("${spring.kafka.topics}")
	private String topic;

	@Autowired
	private KafkaTemplate<String, Product> kafkaTemplate;

	@Override
	public void send(Product data) {

		log.info("sending data='{}' to topic='{}'", data, topic);
		Message<Product> message = MessageBuilder.withPayload(data).setHeader(KafkaHeaders.TOPIC, topic).build();
		kafkaTemplate.send(message);
	}
}
