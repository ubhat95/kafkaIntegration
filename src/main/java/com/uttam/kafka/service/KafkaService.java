package com.uttam.kafka.service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uttam.kafka.enums.KafkaConsumerGroup;

@Service
public class KafkaService {
	
	@Autowired
	KafkaProducerService kafkaProducerService;
	
	@Autowired
	KafkaConsumerService kafkaConsumerService;

	public boolean sendWithKafkaTemplate(String userTopic, String key, Object msg) {
		return kafkaProducerService.sendWithKafkaTemplate(userTopic, key, msg);
		
		
	}

	public boolean sendWithKafkaProducer(String userkafkatopic, String key, Object msg) {
		return kafkaProducerService.sendWithKafkaProducer(userkafkatopic, key, msg);
	}

	public <T> void registerConsumer(String topic, KafkaConsumerGroup consumerGroup, Class<T> clazz, Consumer<T> callBack,
			Consumer<List<T>> batchCallBack, Duration seconds, Properties properties, boolean commitOffset) {
		 kafkaConsumerService.registerConsumer(topic,consumerGroup,clazz,callBack,batchCallBack,seconds,properties,commitOffset);
		
	}

}
