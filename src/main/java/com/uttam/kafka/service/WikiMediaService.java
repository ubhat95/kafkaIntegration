package com.uttam.kafka.service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.launchdarkly.eventsource.EventSource;
import com.uttam.kafka.consumer.OpenSearchConsumer;

@Service
public class WikiMediaService {
	
	@Value("${wikimedia_url:https://stream.wikimedia.org/v2/stream/recentchange}")
	private String wikimediaUrl;
	
	@Autowired
	WikiMediaEventHandler wikiMediaEventHandler;
	
	@Autowired
	OpenSearchConsumer openSearchConsumer;
	
	public void produceAndConsume() throws InterruptedException{
		openSearchConsumer.register();
        EventSource.Builder builder = new EventSource.Builder(wikiMediaEventHandler, URI.create(wikimediaUrl));
        EventSource eventSource = builder.build();
        eventSource.start();
        TimeUnit.MINUTES.sleep(1);
        eventSource.close();
	}
}
