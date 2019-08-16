package com.commercetools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author PoojaShankar
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(topics = { "product" })
@TestPropertySource(properties = { "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
"spring.kafka.consumer.auto-offset-reset=latest"})
public class AggregatorServiceApplicationTests {

	@Test
	public void contextLoads() {
	}

}
