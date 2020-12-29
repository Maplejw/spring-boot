package com.igg.boot.framework.autoconfigure.es.condition;

import org.elasticsearch.index.query.QueryBuilder;

import lombok.Data;

@Data
public class EsQueryBuilder {
    private QueryBuilder queryBuilder;
    private String routing;

    public EsQueryBuilder(QueryBuilder queryBuilder) {
        this(queryBuilder,"");
    }

    public EsQueryBuilder(QueryBuilder queryBuilder, String routing) {
        this.queryBuilder = queryBuilder;
        this.routing = routing;
    }
}
