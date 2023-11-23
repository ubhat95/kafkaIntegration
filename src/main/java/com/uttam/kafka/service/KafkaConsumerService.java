package com.uttam.kafka.service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uttam.kafka.consumer.KafkaConsumerFactory;
import com.uttam.kafka.enums.KafkaConsumerGroup;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumerService {
	
	@Autowired
	KafkaConsumerFactory kafkaConsumerFactory;
	
	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	
	public <T> void registerConsumer(String topic, KafkaConsumerGroup consumerGroup, Class<T> clazz,
			Consumer<T> callBack, Consumer<List<T>> batchCallBack, Duration seconds, Properties properties,
			boolean commitOffset) {
			Runnable consumerProcessor = () ->{
				try(KafkaConsumer<String, byte[]> kafkaConsumer = kafkaConsumerFactory.getKafkaConsumer(consumerGroup.name(), properties)){
					kafkaConsumer.subscribe(Collections.singleton(topic));
					log.info("Thread {} to consume message from kafka {} topic started",Thread.currentThread().getName() ,topic);
					do {
						try {
							pollAndProcessConsumedRecords(clazz,callBack,batchCallBack,commitOffset, kafkaConsumer);
						} catch (WakeupException e) {
							 log.info("Consumer thread received wakeup signal. Shutting down gracefully...");
		                        break;
						}
					}while(!Thread.interrupted());
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
			startConsumer(consumerProcessor,consumerGroup, topic);
	}

	private <T> void pollAndProcessConsumedRecords(Class<T> clazz, Consumer<T> callBack, Consumer<List<T>> batchCallBack,
			boolean commitOffset, KafkaConsumer<String, byte[]> kafkaConsumer) {
		ConsumerRecords<String, byte[]> consumerRecords = kafkaConsumer.poll( Duration.ofMillis(1000));
		if(callBack!=null) {
			consumerRecords.forEach(i -> processRecord(i,clazz,callBack));
		}
		
	}

	private <T> void processRecord(ConsumerRecord<String, byte[]> record, Class<T> clazz, Consumer<T> callBack) {
		try {
			T data = objectMapper.readValue(record.value(), clazz);
			callBack.accept(data);
		} catch (Exception e) {
			log.error("Exception while processing consumer record");
		} finally {
			
		}
	}

	private void startConsumer(Runnable consumerProcessor, KafkaConsumerGroup consumerGroup, String topic) {
		Thread thread = getThread(consumerProcessor, "KafkaConsumer-"+consumerGroup);
		thread.start();
		
		 Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	            log.info("Shutting down gracefully...");
	            thread.interrupt(); // Wake up the consumer thread
	            try {
	                thread.join(); // Wait for the consumer thread to complete
	            } catch (InterruptedException e) {
	                log.error("Interrupted while waiting for consumer thread to complete", e);
	            }
	        }));
		
	}

	private Thread getThread(Runnable consumerProcessor, String threadName) {
		Thread thread = new Thread(consumerProcessor);
		thread.setDaemon(false);
		thread.setName(threadName);
		return thread;
	}

}
