package com.uttam.kafka.producer;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.uttam.kafka.enums.TOPIC;
import com.uttam.kafka.service.KafkaService;

import lombok.Data;

@Service
@Data
public class WikiMediaProducer {
	
	@Autowired
	KafkaService kafkaService;
	
	private final static String WK_MEDIA = TOPIC.WIKIMEDIA.getName();
	
	public Set<String> pushKafkaMsgGetFailedIds(Set<String> messages){
		
		Set<String> failedIds = new HashSet<>();
		if(!CollectionUtils.isEmpty(messages)) {
			messages.forEach( msg -> {
				boolean successful = kafkaService.sendWithKafkaProducer(WK_MEDIA, msg, msg);
				if(!successful)failedIds.add(msg);
			});
		}
		
		return failedIds;
	}
}
