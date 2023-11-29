package com.uttam.kafka.producer;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.uttam.kafka.dto.User;
import com.uttam.kafka.enums.Topic;
import com.uttam.kafka.service.KafkaService;

@Component
public class UserKafkaProducer {

	@Autowired
	KafkaService kafkaService;
	
	private final static String USERKAFKATOPIC = Topic.USER.getName();
	
	public Set<Integer> pushKafkaMsgGetFailedIds(Set<User> messages){
		
		Set<Integer> failedIds = new HashSet<>();
		if(!CollectionUtils.isEmpty(messages)) {
			messages.forEach( msg -> {
				boolean successful = kafkaService.sendWithKafkaProducer(USERKAFKATOPIC, msg.getId().toString(), msg);
				if(!successful)failedIds.add(msg.getId());
			});
		}
		
		return failedIds;
	}
}
