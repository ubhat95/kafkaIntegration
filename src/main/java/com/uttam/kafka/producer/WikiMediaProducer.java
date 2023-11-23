package com.uttam.kafka.producer;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<String> pushKafkaMsgGetFailedIds(List<String> messages){
		
		List<String> failedIds = new ArrayList<String>();
		if(!CollectionUtils.isEmpty(messages)) {
			messages.forEach( msg -> {
				boolean successful = kafkaService.sendWithKafkaProducer(WK_MEDIA, msg, msg);
				if(!successful)failedIds.add(msg);
			});
		}
		
		return failedIds;
	}

}
