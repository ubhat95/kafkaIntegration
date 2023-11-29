package com.uttam.kafka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OSSearchHit {
	private String id;
	private String index;
	private Float score;
}
	