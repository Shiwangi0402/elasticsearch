package com.example.elasticsearch.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.helper.Util;

@Service
public class IndexService {
private final List<String> indicesToCreate = List.of(Indices.VEHICLE_INDEX);
@Autowired
private final RestHighLevelClient client;

public IndexService(RestHighLevelClient client) {
	this.client = client;
}

@PostConstruct
public void tryToCreateIndices() {
	recreateIndices(false);
}

public void recreateIndices(boolean deleteExisting) {
	final String settings = Util.loadAsString("static/es-settings.json");
	 if(settings == null) {
		 System.out.println("ERROR: Failed to load index settings");
		 return;
	 }
	
	for (String index: indicesToCreate) {
		boolean indexExist;
		try {
			indexExist = client.indices()
					.exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
			if(indexExist) {
				if(!deleteExisting) {
					continue;	
				}
    			client.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
    		}
			
			final String mappings = Util.loadAsString("static/mappings/"+index+".json");
			if (settings == null || mappings == null) {
				System.out.println("ERROR: Failed to create index: "+ index);
				continue;
			}
			
			final CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
			createIndexRequest.settings(settings, XContentType.JSON);
			createIndexRequest.mapping(mappings, XContentType.JSON);
			client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
}
