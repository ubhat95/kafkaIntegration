package com.uttam.kafka.enums;

import lombok.Getter;

@Getter
public enum TOPIC {
	USER("user_topic"),
	WIKIMEDIA("wikimedia_topic");
	
	String name;
	
	TOPIC(String name) {
		this.name=name;
	}
}
