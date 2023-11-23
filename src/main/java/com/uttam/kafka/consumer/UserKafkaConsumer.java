package com.uttam.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.uttam.kafka.dto.User;

@Component
public class UserKafkaConsumer {
	
	@KafkaListener(topics = "user_topic", 
			groupId = "userRegistrationGroup", 
			containerFactory = "kafkaListenerContainerFactory",
			concurrency="3" )
	
    public void listen(User message) {
        System.out.println("Received message: " + message.getName());
    }
	
}
