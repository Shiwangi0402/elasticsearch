package com.example.elasticsearch.search.util;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MultiMatchQueryParser;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

import com.example.elasticsearch.search.SearchRequestDTO;

public final class SearchUtil {

	private SearchUtil() {}
	
	public static SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO dto) {
		try {
			
			final int page = dto.getPage();
			final int size = dto.getSize();
			final int from = page <= 0 ? 0 : page * size;
			SearchSourceBuilder builder = new SearchSourceBuilder()
					.from(from)
					.size(size)
					.postFilter(getQueryBuilder(dto));
			if(dto.getSortBy() != null) {
				builder = builder.sort(dto.getSortBy(), 
						dto.getOrder() != null ? dto.getOrder() : SortOrder.ASC);
			}
			SearchRequest request = new SearchRequest(indexName);
			request.source(builder);
			return request;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static SearchRequest buildSearchRequest(final String indexName, final String field, final Date date) {
		try {
			SearchSourceBuilder builder = new SearchSourceBuilder()
					.postFilter(getQueryBuilder(field, date));
			
			final SearchRequest request = new SearchRequest(indexName);
			request.source(builder);
			return request;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	private static QueryBuilder getQueryBuilder(SearchRequestDTO dto) {
		if (dto == null) {
			return null;
		}
		
		final List<String> fields = dto.getFields();
		if(CollectionUtils.isEmpty(fields)) {
			return null;
		}
		
		if (fields.size() > 1) {
			MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(dto.getSearchTerm())
					.type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
					.operator(Operator.AND);
			fields.forEach(queryBuilder::field);
			return queryBuilder;
		}
		
		return fields.stream()
				.findFirst()
				.map(field ->
				       QueryBuilders.matchQuery(field, dto.getSearchTerm())
				        .operator(Operator.AND
				        		))
				.orElse(null);
	}
	
	private static QueryBuilder getQueryBuilder(final String field, final Date date) {
		return QueryBuilders.rangeQuery(field).gte(date);
	}
	
	public static SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO dto, final Date date) {
		try {
			QueryBuilder searchQuery = getQueryBuilder(dto);
			QueryBuilder dateQuery = getQueryBuilder("created", date);
			
			final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
					.must(searchQuery)
					.must(dateQuery);
			
			SearchSourceBuilder builder = new SearchSourceBuilder()
					.postFilter(queryBuilder);
			if(dto.getSortBy() != null) {
				builder = builder.sort(dto.getSortBy(), 
						dto.getOrder() != null ? dto.getOrder() : SortOrder.ASC);
			}
			SearchRequest request = new SearchRequest(indexName);
			request.source(builder);
			return request;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
