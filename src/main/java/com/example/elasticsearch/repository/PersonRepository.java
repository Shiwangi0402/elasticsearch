package com.example.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.elasticsearch.document.Person;

public interface PersonRepository extends ElasticsearchRepository<Person, String>{

}
