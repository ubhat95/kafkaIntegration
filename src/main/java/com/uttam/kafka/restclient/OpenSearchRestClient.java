package com.uttam.kafka.restclient;

import java.net.URI;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OpenSearchRestClient {

	public static RestHighLevelClient getClient() {
		try {
		String connString = "http://localhost:9200";
		RestHighLevelClient restHighLevelClient;
		URI connUri = URI.create(connString);
		restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), "http")));
		
		return restHighLevelClient;
		} catch (Exception e) {
			log.error("error creating client ");
		}
		return null;
	}
}
