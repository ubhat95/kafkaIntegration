package com.uttam.kafka.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uttam.kafka.consumer.OpenSearchConsumer;
import com.uttam.kafka.producer.WikiMediaProducer;

@Service
public class wikiMediaService {
	
	@Autowired
	WikiMediaProducer wikiMediaProducer;
	
	@Autowired
	OpenSearchConsumer openSearchConsumer;
	
	public Set<String> produceAndConsume(List<String> messages){
		openSearchConsumer.register();
		return wikiMediaProducer.pushKafkaMsgGetFailedIds(new HashSet<>(messages));
	}
}
