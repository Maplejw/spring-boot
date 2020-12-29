package com.igg.boot.framework.autoconfigure.db;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "igg.db.druid")
@Data
public class DruidDataSourceProperties {
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	private String filters;
	private int initialSize = 2;
	private int maxActive = 100;
	private int minIdle = 4;
	private int maxWait = 10000;
	private int timeBetweenEvictionRunsMillis = 60000;
	private int minEvictableIdleTimeMillis = 300000;
	private String validationQuery = "select \'x\'";
	private int validationQueryTimeout = -1;
	private boolean testWhileIdle = true;
	private boolean testOnBorrow = false;
	private boolean testOnReturn = false;
	private boolean unfairLock = true;
	private boolean poolPreparedStatements = false;
	private int maxPoolPreparedStatementPerConnectionSize = -1;
	private int maxOpenPreparedStatements = 20;
	private boolean shardTable;
}
