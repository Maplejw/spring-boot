package com.igg.boot.framework.es.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import com.igg.boot.framework.autoconfigure.es.Routing;

import lombok.Data;


@Document(indexName = "vpntrial",type="maindata",replicas=1,shards=3,refreshInterval="30s")
@Mapping
@Data
public class VpnTrial {
    @Id
    private String id;
    
    private String userName;
    
    private String country; 
    
    @Routing("add_time")
    private int addTime;
}
