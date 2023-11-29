package com.uttam.kafka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uttam.kafka.dto.EntityContext;
import com.uttam.kafka.service.FullSearchService;

@RestController
@RequestMapping("/fullsearch")
@Component
public class FullsearchController {
	
	@Autowired
	FullSearchService fullSearchService;

	@GetMapping("/lookup")
	public List<EntityContext> search(@RequestParam String searchTerm, @RequestParam String index, @RequestParam String sortField) {
		return fullSearchService.search(searchTerm, index,sortField);
		
	}
}
