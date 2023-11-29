package com.uttam.kafka.commons;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaObjectSerializer<T> implements Serializer<T>{

	@Override
	public byte[] serialize(String topic, T object) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
		try {
			return objectMapper.writeValueAsBytes(object);
		}catch (Exception e) {
			log.info("cannot serialize for topic: {} , object: {}", topic, object);
		}
		return null;
	}

}
