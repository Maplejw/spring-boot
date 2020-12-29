package com.igg.boot.framework.autoconfigure.db;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "igg.db.transaction")
@Data
public class DBTransactionProperties {
	private String method = "save*,add*,update*,delete*";
	private String expression = "execution(* cn.onemt..*.service..*.*(..))";
	private int timeout = 10;
}
