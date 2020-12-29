package com.igg.boot.framework.autoconfigure.es;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "igg.es")
@Data
public class ElastisearchProperties {
	private String clusterName;
	private String clusterNodes;
	private Map<String, String> properties = new HashMap<>();
	private boolean lowerCaseWithUnderscores = true;
}
