package com.example.elasticsearch.search;

public class PageRequestDTO {

	private static final int DEFAULT_SIZE = 100;
	private int size;
	private int page;
	public int getSize() {
		return size != 0 ? size : DEFAULT_SIZE;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	
}
