package com.igg.boot.framework.autoconfigure.es;

import java.util.Arrays;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.igg.boot.framework.autoconfigure.es.ElasticsearchAutoconfiguration.IggElasticsearchTemplate;
import com.igg.boot.framework.autoconfigure.es.condition.AndCondition;
import com.igg.boot.framework.autoconfigure.es.condition.EsQueryBuilder;

public class ElasticsearchDao {
    private IggElasticsearchTemplate elasticsearchTemplate;
    
    public ElasticsearchDao(IggElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    private NativeSearchQueryBuilder createNativeSearchQueryBuilder(Class<?> clz,String routing) {
        Document document = ElastisearchParse.getDoucment(clz);
        
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().withIndices(document.indexName())
                .withSearchType(SearchType.DEFAULT).withTypes(document.type());
        if(StringUtils.isEmpty(routing)) {
            builder.withRoute(routing);
        }
        
        return builder;
    }

    public <T> List<T> query(AndCondition condition, Class<T> clz) {
        return query(condition, clz, null, null);
    }

    public <T> List<T> query(AndCondition condition, Class<T> clz, PageRequest page) {
        return query(condition, clz, page, null);
    }

    public <T> List<T> query(AndCondition condition, Class<T> clz, Sort sort) {
        return query(condition, clz, null, sort);
    }
    
    public <T> void saveWithRouting(List<T> model) {
        elasticsearchTemplate.bulkIndexWithRouting(model);
    }
    
    public <T> void saveWithRouting(T model) {
        elasticsearchTemplate.bulkIndexWithRouting(Arrays.asList(model));
    }
    
    public <T> String save(T model) {
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setObject(model);
        
        return elasticsearchTemplate.index(indexQuery);
    }

    public <T> List<T> query(AndCondition condition, Class<T> clz, PageRequest page, Sort sort) {
        EsQueryBuilder esQueryBuilder = condition.toEsQueryBuilder();
        NativeSearchQueryBuilder builder = createNativeSearchQueryBuilder(clz,esQueryBuilder.getRouting());
        builder.withQuery(esQueryBuilder.getQueryBuilder());
        if (page != null) {
            builder.withPageable(page);
        }
        SearchQuery searchQuery = builder.build();
        if (sort != null) {
            searchQuery.addSort(sort);
        }
        List<T> list = elasticsearchTemplate.queryForList(searchQuery, clz);

        return list;
    }
    
    public <T> long count(Class<T> clz, AndCondition condition) {
        EsQueryBuilder esQueryBuilder = condition.toEsQueryBuilder();
        NativeSearchQueryBuilder builder = createNativeSearchQueryBuilder(clz,esQueryBuilder.getRouting());
        builder.withQuery(esQueryBuilder.getQueryBuilder());
        SearchQuery searchQuery = builder.build();
        
        return elasticsearchTemplate.count(searchQuery);
    }

    public <T> Aggregations queryAggregations(Class<T> clz, AndCondition condition,
            List<AbstractAggregationBuilder<?>> aggregationBuilders, SortBuilder<?> sortBuilder) {
        EsQueryBuilder esQueryBuilder = condition.toEsQueryBuilder();
        NativeSearchQueryBuilder builder = createNativeSearchQueryBuilder(clz,esQueryBuilder.getRouting());
        builder.withQuery(esQueryBuilder.getQueryBuilder());
        if (sortBuilder != null) {
            builder.withSort(sortBuilder);
        }
        aggregationBuilders.forEach(aggregationBuilder -> {
            builder.addAggregation(aggregationBuilder);
        });
        SearchQuery searchQuery = builder.build();
        Aggregations aggretions = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        return aggretions;
    }

    public <T> Aggregations queryAggregations(Class<T> clz, AndCondition condition,
            AbstractAggregationBuilder<?> aggregationBuilder) {
        return queryAggregations(clz, condition, Arrays.asList(aggregationBuilder), null);
    }

    public <T> Aggregations queryAggregations(Class<T> clz, AndCondition condition,
            AbstractAggregationBuilder<?> aggregationBuilder, SortBuilder<?> sortBuilders) {
        return queryAggregations(clz, condition, Arrays.asList(aggregationBuilder), sortBuilders);
    }

    public <T> T queryAggregations(Class<T> clz, AndCondition condition,
            AbstractAggregationBuilder<?> aggregationBuilder,
            ElastisearchAggretaionExtract<T> elastisearchAggretaionExtract) {
        return queryAggregations(clz, condition, aggregationBuilder, null, elastisearchAggretaionExtract);
    }

    public <T> T queryAggregations(Class<T> clz, AndCondition condition,
            AbstractAggregationBuilder<?> aggregationBuilder, SortBuilder<?> sortBuilders,
            ElastisearchAggretaionExtract<T> elastisearchAggretaionExtract) {
        Aggregations aggretions = queryAggregations(clz, condition, Arrays.asList(aggregationBuilder),
                sortBuilders);

        return elastisearchAggretaionExtract.invoke(aggretions);
    }

}
