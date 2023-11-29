package com.uttam.kafka.enums;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum Topic {
	USER("user_topic"),
	WIKIMEDIA("wikimedia_topic");
	
	String name;
	
	Topic(String name) {
		this.name=name;
	}
	
	public static Topic getByName(String name) {
		return Arrays.stream(Topic.values()).filter(topic->topic.getName().equals(name)).findFirst().get();
	}
}
