package com.uttam.kafka.consumer;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uttam.kafka.enums.KafkaConsumerGroup;
import com.uttam.kafka.enums.TOPIC;
import com.uttam.kafka.service.KafkaService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OpenSearchConsumer {
	
	private final static String OPENSEARCH_TOPIC = TOPIC.WIKIMEDIA.getName();
	
	@Value("${opensearch_consumer_count:1}")
	private Integer consumersCount;
	

	@Autowired
	KafkaService kafkaService;
	
	public void register() {
		Properties properties =  new Properties();
		properties.setProperty("bootstrap.servers", "localhost:9092");
	    
	    for(int i=0;i<consumersCount;i++) {
	    	kafkaService.registerConsumer(OPENSEARCH_TOPIC, KafkaConsumerGroup.OPEN_SEARCH_GROUP, String.class, message ->{
	    	callBackFunction(message);	
	    	}, null, 1000l, properties, true);
	    }
	}
	
	public void callBackFunction(String message) {
		log.info("consumed {}", message);
	}
}
