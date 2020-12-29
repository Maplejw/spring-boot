package com.igg.boot.framework.autoconfigure.db;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.igg.boot.framework.jdbc.persistence.Entity;
import com.igg.boot.framework.jdbc.persistence.shard.ShardStrategy;
import com.igg.boot.framework.jdbc.persistence.shard.ShardStrategyMap;

@Configuration
@ConditionalOnClass(DruidDataSource.class)
@EnableConfigurationProperties(DruidDataSourceProperties.class)
public class DBDefaultAutoconfiguration {
	@Autowired
	private DruidDataSourceProperties dbProperties;
	public static final String PRIMARY_DATASOURCE = "primary_db_datasource";
	
	@Bean(PRIMARY_DATASOURCE)
	@ConditionalOnMissingBean(DataSource.class)
	public DataSource dataSource() {
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(dbProperties.getUrl());
		datasource.setUsername(dbProperties.getUsername());
		datasource.setPassword(dbProperties.getPassword());
		datasource.setDriverClassName(dbProperties.getDriverClassName());
		datasource.setInitialSize(dbProperties.getInitialSize());
		datasource.setMinIdle(dbProperties.getMinIdle());
		datasource.setMaxActive(dbProperties.getMaxActive());
		datasource.setMaxWait(dbProperties.getMaxWait());
		datasource.setTimeBetweenEvictionRunsMillis(dbProperties.getTimeBetweenEvictionRunsMillis());
		datasource.setMinEvictableIdleTimeMillis(dbProperties.getMinEvictableIdleTimeMillis());
		datasource.setValidationQuery(dbProperties.getValidationQuery());
		datasource.setValidationQueryTimeout(dbProperties.getValidationQueryTimeout());
		datasource.setTestWhileIdle(dbProperties.isTestWhileIdle());
		datasource.setTestOnBorrow(dbProperties.isTestOnBorrow());
		datasource.setTestOnReturn(dbProperties.isTestOnReturn());
		datasource.setPoolPreparedStatements(dbProperties.isPoolPreparedStatements());
		datasource.setMaxPoolPreparedStatementPerConnectionSize(dbProperties.getMaxPoolPreparedStatementPerConnectionSize());
		datasource.setMaxOpenPreparedStatements(dbProperties.getMaxOpenPreparedStatements());
		datasource.setUseUnfairLock(dbProperties.isUnfairLock());
		
		return datasource;
	}
	
	
	
	@Bean
	@ConditionalOnMissingBean(ShardStrategyMap.class)
	@ConditionalOnProperty(prefix = "igg.db.druid",name="shardTable",matchIfMissing = false)
	public ShardStrategyMap shardStrategyMapList(List<ShardStrategy<? extends Entity>> list) {
		return new ShardStrategyMap(list);
	}
	
	@Bean
	@ConditionalOnMissingBean(ShardStrategyMap.class)
	public ShardStrategyMap shardStrategyMap() {
		List<ShardStrategy<? extends Entity>> list = new ArrayList<>(1);
		return new ShardStrategyMap(list);
	}
	
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier(PRIMARY_DATASOURCE) DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
}
