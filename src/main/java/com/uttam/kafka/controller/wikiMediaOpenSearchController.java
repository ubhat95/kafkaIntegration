package com.uttam.kafka.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uttam.kafka.service.wikiMediaService;

@Component
@RestController
@RequestMapping("/wmos")
public class wikiMediaOpenSearchController {
	
	
	@Autowired
	wikiMediaService wikiMediaService;
	
	@PostMapping("/run")
	public List<String> produceAndConsume(@RequestBody List<String> messages) {
		return wikiMediaService.produceAndConsume(messages);
	}
}
