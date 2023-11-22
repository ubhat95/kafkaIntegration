package com.uttam.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
	
	
	@Autowired
	KafkaProducerService kafkaProducerService;

	public boolean sendWithKafkaTemplate(String userTopic, String key, Object msg) {
		return kafkaProducerService.sendWithKafkaTemplate(userTopic, key, msg);
		
		
	}

	public boolean sendWithKafkaProducer(String userkafkatopic, String key, Object msg) {
		return kafkaProducerService.sendWithKafkaProducer(userkafkatopic, key, msg);
	}

}
