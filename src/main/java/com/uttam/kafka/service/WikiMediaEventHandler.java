package com.uttam.kafka.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import com.uttam.kafka.producer.WikiMediaProducer;

@Component
public class WikiMediaEventHandler  implements EventHandler{
	
	@Autowired
	WikiMediaProducer wikiMediaProducer;

	@Override
	public void onOpen() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClosed() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String event, MessageEvent messageEvent) throws Exception {
		wikiMediaProducer.pushKafkaMsgGetFailedIds(Collections.singletonList(messageEvent.getData()));
		
	}

	@Override
	public void onComment(String comment) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Throwable t) {
		// TODO Auto-generated method stub
		
	}

}
