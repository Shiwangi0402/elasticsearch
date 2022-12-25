package com.example.elasticsearch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elasticsearch.document.Vehicle;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.helper.Util;
import com.example.elasticsearch.search.SearchRequestDTO;
import com.example.elasticsearch.search.util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class VehicleService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
	private final RestHighLevelClient client;

	public VehicleService(RestHighLevelClient client) {
		this.client = client;
	}
	
	public Boolean index(Vehicle vehicle) {
		try {
			final String vehicleAsString = MAPPER.writeValueAsString(vehicle);
			final IndexRequest request = new IndexRequest(Indices.VEHICLE_INDEX);
			request.id(vehicle.getId());
			request.source(vehicleAsString, XContentType.JSON);
			
			IndexResponse response = client.index(request, RequestOptions.DEFAULT);
			
			return response != null && response.status().equals(RestStatus.CREATED);
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
    
	public Vehicle getById(final String vehicleId) {
		try {
			final GetResponse documentFields = client.get(new GetRequest(Indices.VEHICLE_INDEX, vehicleId), RequestOptions.DEFAULT);
			if(documentFields == null)
				return null;
			
			return MAPPER.readValue(documentFields.getSourceAsString(), Vehicle.class);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<Vehicle> search(final SearchRequestDTO dto) {
		final SearchRequest request = SearchUtil.buildSearchRequest(Indices.VEHICLE_INDEX, dto);
		
		return searchInternal(request);
	}

	public List<Vehicle> getAllVehicleCreatedSince(final Date date) {
       final SearchRequest request = SearchUtil.buildSearchRequest(Indices.VEHICLE_INDEX,
    	    "created", date);
		
		return searchInternal(request);
	}
	private List<Vehicle> searchInternal(final SearchRequest request) {
	if(request == null) {
		System.out.println("ERROR: Failed to build search requets");
		return Collections.EMPTY_LIST;
	}
	
	try {
		final SearchResponse response = client.search(request, RequestOptions.DEFAULT);
		
		final SearchHit[] searchhits = response.getHits().getHits();
		final List<Vehicle> vehicles = new ArrayList<>(searchhits.length);
		for(SearchHit hit: searchhits) {
			
			vehicles.add(MAPPER.readValue(hit.getSourceAsString(), Vehicle.class));
		}
		return vehicles;
	}catch(Exception e) {
		e.printStackTrace();
		return null;
	}}

	public List<Vehicle> searchCreatedSince(final SearchRequestDTO dto, final Date date) {
       final SearchRequest request = SearchUtil.buildSearchRequest(Indices.VEHICLE_INDEX, dto, date);
		
		return searchInternal(request);
	}
}
