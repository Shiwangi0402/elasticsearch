package com.example.elasticsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elasticsearch.document.Person;
import com.example.elasticsearch.service.PersonService;

@RestController
@RequestMapping("/api/person")
public class PersonController {

	@Autowired
	private final PersonService service;
	
	
	public PersonController(PersonService service) {
		this.service = service;
	}
	
	@PostMapping
	public Person save(@RequestBody Person person) {
		return service.save(person);
	}
	
	@GetMapping("/{id}")
	public Person findById(@PathVariable final String id) {
		return service.findById(id);
	}
}
