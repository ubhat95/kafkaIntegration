package com.uttam.kafka.enums;

import lombok.Getter;

@Getter
public enum TOPIC {
	USER_TOPIC("first_topic");
	
	String name;
	
	TOPIC(String name) {
		this.name=name;
	}
}
