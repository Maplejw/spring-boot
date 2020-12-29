package com.igg.boot.framework.rest.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="igg.rest")
public class RestConfiguration {
    private int connectTimeout = 2000;
    private int readTimeout = 3000;
    private int writeTimeout = 5000;
    private int maxIdleConnect = 10;
    private int keepAliveDuration = 5; //单位:分钟
}
