package com.uttam.kafka.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uttam.kafka.commons.KafkaConsumerFactory;
import com.uttam.kafka.enums.KafkaConsumerGroup;

import jakarta.annotation.PreDestroy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumerService {

	@Autowired
	KafkaConsumerFactory kafkaConsumerFactory;

	@Setter
	private List<Thread> gracefulShutdown = new ArrayList<Thread>();

	@Setter
	private ReentrantLock gracefulLock = new ReentrantLock();

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	public <T> void registerConsumer(String topic, KafkaConsumerGroup consumerGroup, Class<T> clazz,
			Consumer<T> callBack, Consumer<List<T>> batchCallBack, long milliseconds, Properties properties,
			boolean commitOffset) {

		Runnable consumerProcessor = () -> {
			try (KafkaConsumer<String, byte[]> kafkaConsumer = kafkaConsumerFactory
					.getKafkaConsumer(consumerGroup.name(), properties)) {

				kafkaConsumer.subscribe(Collections.singleton(topic));
				log.info("Thread {} to consume message from kafka {} topic started", Thread.currentThread().getName(),
						topic);

				do {
					pollAndProcessConsumedRecords(clazz, callBack, batchCallBack, commitOffset, kafkaConsumer,
							milliseconds);

				} while (!Thread.interrupted());

			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		startConsumer(consumerProcessor, consumerGroup, topic);
	}

	private <T> void pollAndProcessConsumedRecords(Class<T> clazz, Consumer<T> callBack,
			Consumer<List<T>> batchCallBack, boolean commitOffset, KafkaConsumer<String, byte[]> kafkaConsumer,
			long milliseconds) {
		ConsumerRecords<String, byte[]> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(milliseconds));
		if (callBack != null) {
			consumerRecords.forEach(i -> processRecord(i, clazz, callBack));
		} else if (batchCallBack != null) {
			processRecord(consumerRecords, clazz, batchCallBack);
		}
		if(commitOffset)kafkaConsumer.commitSync();
	}

	private <T> void processRecord(ConsumerRecords<String, byte[]> records, Class<T> clazz,
			Consumer<List<T>> batchCallBack) {
		List<T> dataList = new ArrayList<T>();
		
		for(ConsumerRecord<String,byte[]> cR : records) {
			try {
			
				T data = objectMapper.readValue(cR.value(), clazz);
				dataList.add(data);
			}catch (Exception e) {
			log.error("Exception while processing consumer record");
			}
		}
		batchCallBack.accept(dataList);
	}

	private <T> void processRecord(ConsumerRecord<String, byte[]> record, Class<T> clazz, Consumer<T> callBack) {
		try {
			T data = objectMapper.readValue(record.value(), clazz);
			callBack.accept(data);
		} catch (Exception e) {
			log.error("Exception while processing consumer record");
		}
	}

	private void startConsumer(Runnable consumerProcessor, KafkaConsumerGroup consumerGroup, String topic) {
		Thread thread = getThread(consumerProcessor, "KafkaConsumer-" + consumerGroup);
		try {
			gracefulLock.lock();
			gracefulShutdown.add(thread);
		} finally {
			gracefulLock.unlock();
		}
		thread.start();

	}

	private Thread getThread(Runnable consumerProcessor, String threadName) {
		Thread thread = new Thread(consumerProcessor);
		thread.setDaemon(false);
		thread.setName(threadName);
		return thread;
	}

	@PreDestroy
	void close() {
		for (Thread thread : gracefulShutdown) {
			log.info("Shutting down thread {} gracefully", thread.getName());
			thread.interrupt();
		}
	}

}
