package com.igg.boot.framework.autoconfigure.es.condition;

import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class ExitCondition extends Condition {
	private String key;

	public ExitCondition(String key) {
		this.key = key;
	}

	@Override
	public QueryBuilder toQueryBuilder() {
	    ExistsQueryBuilder exitQueryBuilder = QueryBuilders.existsQuery(key);
		
		return exitQueryBuilder;
	}
}
