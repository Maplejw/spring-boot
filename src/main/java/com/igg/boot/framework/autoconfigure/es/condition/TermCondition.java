package com.igg.boot.framework.autoconfigure.es.condition;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

public class TermCondition extends Condition {
	private String key;
	private Object value;

	public TermCondition(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public QueryBuilder toQueryBuilder() {
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key, value);
		
		return termQueryBuilder;
	}
}
