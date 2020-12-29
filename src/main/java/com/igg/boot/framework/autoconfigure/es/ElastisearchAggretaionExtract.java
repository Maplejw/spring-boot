package com.igg.boot.framework.autoconfigure.es;

import org.elasticsearch.search.aggregations.Aggregations;

public interface ElastisearchAggretaionExtract<T> {
    T invoke(Aggregations aggretions);
}
