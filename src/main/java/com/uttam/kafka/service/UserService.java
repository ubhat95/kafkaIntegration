package com.uttam.kafka.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uttam.kafka.dto.User;
import com.uttam.kafka.producer.UserKafkaProducer;

@Service
public class UserService {
	
	@Autowired
	UserKafkaProducer userKafkaProducer;
	
	public Set<Integer> registerUsers(List<User> users){
		return userKafkaProducer.pushKafkaMsgGetFailedIds(new HashSet<>(users));
	}
	
}
