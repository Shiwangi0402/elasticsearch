package com.example.elasticsearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elasticsearch.document.Person;
import com.example.elasticsearch.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private final PersonRepository repository;
	
	public PersonService(PersonRepository repository) {
		this.repository = repository;
	}
	
	public Person save(final Person person) {
		return repository.save(person);
	}
	public Person findById(final String id) {
		return repository.findById(id).orElse(null);
	}
}
