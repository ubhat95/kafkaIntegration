package com.uttam.kafka.adapter;

import org.opensearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.uttam.kafka.dto.EntityContext;

@Service
public class OpenSearchAdapter {

	public EntityContext convertToEntityContext(SearchHit openSearchHit) {
		return EntityContext.builder()
				.sourceAsMap(openSearchHit.getSourceAsMap())
				.build();
		}

}
