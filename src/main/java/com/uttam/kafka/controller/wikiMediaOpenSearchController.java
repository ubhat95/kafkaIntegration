package com.uttam.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uttam.kafka.service.wikiMediaService;

@Component
@RestController
@RequestMapping("/wmos")
public class wikiMediaOpenSearchController {
	
	
	@Autowired
	wikiMediaService wikiMediaService;
	
	@GetMapping("/run")
	public void produceAndConsume() {
		wikiMediaService.produceAndConsume();
	}
}
