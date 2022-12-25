package com.example.elasticsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elasticsearch.service.IndexService;

@RestController
@RequestMapping("/api/index")
public class IndexController {

	@Autowired
	private final IndexService service;
	
	
	public IndexController(IndexService service) {
		this.service = service;
	}
	
	@PostMapping("/recreate")
	public void recreateAllIndices() {
		service.recreateIndices(true);
	}
	
}
