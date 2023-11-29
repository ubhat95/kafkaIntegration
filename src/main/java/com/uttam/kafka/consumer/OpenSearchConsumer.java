package com.uttam.kafka.consumer;

import java.util.List;
import java.util.Properties;

import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonParser;
import com.uttam.kafka.enums.KafkaConsumerGroup;
import com.uttam.kafka.enums.Topic;
import com.uttam.kafka.restclient.OpenSearchRestClient;
import com.uttam.kafka.service.KafkaService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OpenSearchConsumer {

	private final static String OPENSEARCH_TOPIC = Topic.WIKIMEDIA.getName();

	@Value("${opensearch_consumer_count:1}")
	private Integer consumersCount;

	@Autowired
	KafkaService kafkaService;
	
	@PostConstruct
	void init() {
		
	}

	public void register() {
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "localhost:9092");

		for (int i = 0; i < consumersCount; i++) {
			kafkaService.registerConsumer(OPENSEARCH_TOPIC, KafkaConsumerGroup.OPEN_SEARCH_GROUP, String.class, null,
					messages -> {
						sendToOpenSearch(messages);
					}, 5000l, properties, true);
		}
	}

	public void sendToOpenSearch(List<String> messages) {
		try(RestHighLevelClient openSearchClient = OpenSearchRestClient.getClient()){
			
			boolean indexExists = openSearchClient.indices().exists(new GetIndexRequest(OPENSEARCH_TOPIC), RequestOptions.DEFAULT);

			if (!indexExists) {
				CreateIndexRequest createIndexRequest = new CreateIndexRequest(OPENSEARCH_TOPIC);
				openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
				log.info("The Wikimedia Index has been created!");
			} else {
				log.info("The Wikimedia Index already exits");
			}
			
		BulkRequest bulkRequest = new BulkRequest();

		for (String message : messages) {
			try {
				//log.info("consumed {}", message);
				String id = extractId(message);

				IndexRequest indexRequest = new IndexRequest(OPENSEARCH_TOPIC).source(message, XContentType.JSON).id(id);

				bulkRequest.add(indexRequest);

			} catch (Exception e) {
				log.error("error extracting id or creating index request");
			}
		}
		
		if (bulkRequest.numberOfActions() > 0) {
			BulkResponse bulkResponse = openSearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
			log.info("Inserted " + bulkResponse.getItems().length + " record(s).");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		} catch (Exception e) {
			log.error("exception", e);
		}
	}

	private static String extractId(String json) {
		return JsonParser.parseString(json).getAsJsonObject().get("meta").getAsJsonObject().get("id").getAsString();
	}
}
