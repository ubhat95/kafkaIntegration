package com.uttam.kafka.consumer;

import java.util.Properties;

import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerFactory {

	
	private Properties getDefaultProperties() {
		Properties properties = new Properties();
		properties.put("key.deserializer",StringDeserializer.class.getName());
		properties.put("value.deserializer", ByteArrayDeserializer.class.getName());
		properties.setProperty("auto.offset.reset", "earliest");
	    properties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());
		return properties;
	}
	
	public KafkaConsumer<String, byte[]> getKafkaConsumer(String consumerGroup, Properties customProperties){
		Properties properties =getDefaultProperties();
		properties.put("group.id", consumerGroup);
		if(customProperties!=null && !customProperties.isEmpty()) {
			properties.putAll(customProperties);
		}
		return new KafkaConsumer<>(properties);
	}
}
