package com.igg.boot.framework.es.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.igg.boot.framework.es.model.TestModel;

public interface TestDao extends ElasticsearchRepository<TestModel, String>{

}
