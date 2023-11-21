package com.uttam.kafka.producer;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.uttam.kafka.dto.User;
import com.uttam.kafka.enums.TOPIC;
import com.uttam.kafka.service.KafkaService;

@Service
public class UserKafkaProducer {

	@Autowired
	KafkaService kafkaService;
	
	public Set<Integer> pushKafkaMsgGetFailedIds(Set<User> messages){
		
		Set<Integer> failedIds = new HashSet<>();
		if(!CollectionUtils.isEmpty(messages)) {
			messages.forEach( msg -> {
				boolean successful = kafkaService.send(TOPIC.USER_TOPIC.getName(), msg.getId().toString(), msg);
				if(!successful)failedIds.add(msg.getId());
			});
		}
		
		return failedIds;
	}
}
