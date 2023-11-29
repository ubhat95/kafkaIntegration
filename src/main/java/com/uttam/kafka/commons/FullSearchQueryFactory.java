package com.uttam.kafka.commons;

import org.opensearch.action.search.SearchRequest;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.QueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.SortBuilders;
import org.opensearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.uttam.kafka.enums.Topic;


@Service
public class FullSearchQueryFactory {

	public SearchRequest formSearchQuery(String searchTerm, String index, String sortField) {
		
		QueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(searchTerm, "_all").field("title", 5).field("user",10);
		//assigns weightage to certain domains altho not a strict filter, potentially rewrite code for a lot of structure and user tuning
        QueryBuilder shouldQuery = QueryBuilders.matchQuery("meta.domain", "simple.wikipedia.org").boost(2);

		BoolQueryBuilder boolQuery = formBasicQuery().must(multiMatchQuery).should(shouldQuery).minimumShouldMatch(1);
		return formSearchRequest(formSearchSourceBuilder(boolQuery, sortField, 5), Topic.getByName(index).getName());
		
	        // Build the search request
	    
	}

	//usually for basic request context filtering in this case just return a boolean query
	private BoolQueryBuilder formBasicQuery() {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		return query;
	}
	
	private SearchSourceBuilder formSearchSourceBuilder(BoolQueryBuilder boolQuery,String sortField, Integer size ) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	    searchSourceBuilder.query(boolQuery);
	    if(sortField!=null)searchSourceBuilder.sort(SortBuilders.fieldSort(sortField).order(SortOrder.ASC));
	    searchSourceBuilder.size(10); // Limit the number of results to 10
	    return searchSourceBuilder;
	}
	
	private SearchRequest formSearchRequest(SearchSourceBuilder searchSourceBuilder, String index) {
		SearchRequest searchRequest = new SearchRequest(index);
	    searchRequest.source(searchSourceBuilder);
	    return searchRequest;
	}

}
