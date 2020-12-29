package com.igg.boot.framework.redis;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix="igg.redis")
@Data
public class RedisProperties {
	private int maxTotal = 100;
	private int maxIdle = 100;
	private int minIdle = 5;
	private int maxWaitMillis = 10000;
	private boolean testOnBorrow = false;
	private boolean testOnReturn = false;
	private boolean testWhileIdle = true;
	private int timeout = 30000;
	
	private int timeBetweenEvictionRunsMillis = 30000;
	private int minEvictableIdleTimeMills = 60000;
	private int numTestsPerEvictionRun = 50;
	private String keyPrefix;
	private String ip;
	private int port;
	private List<String> host;
	private String password;
}
