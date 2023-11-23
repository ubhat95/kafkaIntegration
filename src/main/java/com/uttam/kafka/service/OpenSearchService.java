package com.uttam.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uttam.kafka.consumer.OpenSearchConsumer;

import lombok.Getter;

@Service
@Getter
public class OpenSearchService {
	
	@Autowired
	OpenSearchConsumer openSearchConsumer;
	
	
	public void register() {
		openSearchConsumer.register();
	}
	
}




