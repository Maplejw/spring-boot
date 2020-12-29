package com.igg.boot.framework.autoconfigure.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Configuration
@AutoConfigureBefore(DBDefaultAutoconfiguration.class)
@EnableConfigurationProperties({ DynamicDataSourceProperties.class, DruidDataSourceProperties.class })
@Slf4j
@ConditionalOnClass(DruidDataSource.class)
public class DBDynamicAutoconfiguration {
	@Autowired
	private DynamicDataSourceProperties dynamicProperties;
	@Autowired
	private DruidDataSourceProperties dfProperties;
	
	@Bean
    @ConditionalOnMissingBean(DataSourceSelect.class)
    public DataSourceSelect dataSourceSelect() {
        return new DataSourceSelect();
    }
	
	@Bean(DBDefaultAutoconfiguration.PRIMARY_DATASOURCE)
    @ConditionalOnProperty(prefix = "igg.db.druid.dynamic", name = "enable", matchIfMissing = false)
    public DataSource dynamicDataSource(DataSourceSelect dataSourceSelect) {
        DynamicDataSource dataSource = new DynamicDataSource(dataSourceSelect);
        int size = dynamicProperties.getDataSource().size();
        Map<Object, Object> targetDataSources = new HashMap<>(size + 1);
        DruidDataSource defaultDataSource = getDrudiDataSource(dfProperties);
        targetDataSources.put(DBDefaultAutoconfiguration.PRIMARY_DATASOURCE, defaultDataSource);
        dynamicProperties.getDataSource().forEach((key, dsProperties) -> {
            targetDataSources.put(key, getDrudiDataSource(dsProperties));
        });
        dataSource.setDefaultTargetDataSource(defaultDataSource);
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }
	
	private DruidDataSource getDrudiDataSource(DruidDataSourceProperties dsProperties) {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dsProperties.getUrl());
        datasource.setUsername(dsProperties.getUsername());
        datasource.setPassword(dsProperties.getPassword());
        datasource.setDriverClassName(dsProperties.getDriverClassName());
        try {
            datasource.setFilters(dsProperties.getFilters());
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);
        }
        datasource.setInitialSize(dsProperties.getInitialSize());
        datasource.setMaxActive(dsProperties.getMaxActive());
        datasource.setMinIdle(dsProperties.getMinIdle());
        datasource.setMaxWait(dsProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dsProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dsProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dsProperties.getValidationQuery());
        datasource.setValidationQueryTimeout(dsProperties.getValidationQueryTimeout());
        datasource.setTestWhileIdle(dsProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(dsProperties.isTestOnBorrow());
        datasource.setTestOnReturn(dsProperties.isTestOnReturn());
        datasource.setUseUnfairLock(dsProperties.isUnfairLock());
        datasource.setPoolPreparedStatements(dsProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(dsProperties.getMaxPoolPreparedStatementPerConnectionSize());
        datasource.setMaxOpenPreparedStatements(dsProperties.getMaxOpenPreparedStatements());
        
        return datasource;
    }
	
	public static class DynamicDataSource extends AbstractRoutingDataSource {
        private DataSourceSelect dataSourceSelect;
        
        public DynamicDataSource(DataSourceSelect dataSourceSelect) {
            this.dataSourceSelect = dataSourceSelect;
        }
        
        @Override
        protected Object determineCurrentLookupKey() {
            String key = DBDefaultAutoconfiguration.PRIMARY_DATASOURCE;
            if (StringUtils.hasText(DataSourceHolder.getDatasource())) {
                key = DataSourceHolder.getDatasource();
            }
            return this.dataSourceSelect.select(key);
        }
    }
	
	public static class DataSourceSelect {
        public String select(@NonNull String key) {
            return key;
        }
    }
}
