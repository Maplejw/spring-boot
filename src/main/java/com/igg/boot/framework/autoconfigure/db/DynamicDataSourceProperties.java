package com.igg.boot.framework.autoconfigure.db;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "igg.db.druid.dynamic")
@Data
public class DynamicDataSourceProperties {
	private Map<String, DruidDataSourceProperties> dataSource;
	private boolean enable;
}
