package com.example.elasticsearch.search;

import java.util.List;

import org.elasticsearch.search.sort.SortOrder;

public class SearchRequestDTO extends PageRequestDTO{

	private List<String> fields;
	private String searchTerm;
	private String sortBy;
	private SortOrder order;
	
	public List<String> getFields() {
		return fields;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public SortOrder getOrder() {
		return order;
	}
	public void setOrder(SortOrder order) {
		this.order = order;
	}
	
	
}
