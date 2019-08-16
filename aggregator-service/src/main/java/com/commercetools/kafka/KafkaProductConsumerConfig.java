package com.commercetools.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.commercetools.models.Product;

/**
 * Performs Kafka Product Consumer related Configuration like 
 * Initializing Consumer Factory with all the configurations. 
 * Initializing Kafka Listener Container factory with consumer factory.
 * 
 * @author PoojaShankar
 */

@Configuration
public class KafkaProductConsumerConfig extends KafkaConsumerConfig {

	@Bean
	public ConsumerFactory<String, Product> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
				new JsonDeserializer<>(Product.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Product> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Product> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

}
