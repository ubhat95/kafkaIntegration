package com.uttam.kafka.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uttam.kafka.adapter.OpenSearchAdapter;
import com.uttam.kafka.commons.FullSearchQueryFactory;
import com.uttam.kafka.dto.EntityContext;
import com.uttam.kafka.restclient.OpenSearchRestClient;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FullSearchService {
	
	@Setter
	private RestHighLevelClient openSearchClient;
	
	@Autowired
	FullSearchQueryFactory queryFactory;
	
	@Autowired
	OpenSearchAdapter openSearchAdapter;
	
	@PostConstruct
	void init() {
		openSearchClient = OpenSearchRestClient.getClient();
	}
	
	public List<EntityContext> search(String searchTerm, String index, String sortField) {
		List<EntityContext> results = new ArrayList<>();
		try{
		SearchRequest searchRequest = queryFactory.formSearchQuery(searchTerm, index, sortField);
		SearchResponse searchResponse = openSearchClient.search(searchRequest, RequestOptions.DEFAULT);
		
		log.info("here the response {} ",searchResponse);
		
		for(SearchHit openSearchHit : searchResponse.getHits().getHits()) {
			results.add(openSearchAdapter.convertToEntityContext(openSearchHit));
		}
		} catch (Exception e) {
			log.error("somethign went wrong");
			e.printStackTrace();
		}
		return results;
	}
	@PreDestroy
	void close() throws IOException {
		openSearchClient.close();
	}
}
