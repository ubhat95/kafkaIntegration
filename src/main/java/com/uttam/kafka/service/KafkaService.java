package com.uttam.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
	
	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	public boolean send(String userTopic, String key, Object msg) {
		try{
			kafkaTemplate.send(userTopic,key, msg);
			return true;
		} catch (Exception e) {
			 e.printStackTrace();
			return false;		
		}
		
	}

}
