package com.igg.boot.framework.jdbc.persistence.condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

public class InCondition extends TableNameCondition {
	private static final long serialVersionUID = -313075395842837260L;
	private String column;
	private List<?> parameters;
	private String alias;

	public InCondition() {
		super("");
	}

	public InCondition(String tableName) {
		super(tableName);
	}

	public InCondition(String column, List<?> parameters) {
		this("", column, parameters);
	}

	public InCondition(String tableName, String column, List<?> parameters) {
		this(tableName, column, parameters, "");
	}

	public InCondition(String column, List<?> parameters, String alias) {
		this("", column, parameters, alias);
	}

	public InCondition(String tableName, String column, List<?> parameters, String alias) {
		super(tableName);
		this.column = column;
		this.parameters = parameters;
		this.alias = alias;
		parseColumn();
	}

	public String getColumn() {
		return this.column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}

	public Map<String, Object> getParameters() {
		Map<String, Object> parms = new HashMap<>();
		String key = this.getColumnKeyName();
		parms.put(key, this.parameters);
		return parms;
	}

	private String getColumnKeyName() {
		return StringUtils.isEmpty(this.alias) ? this.column : this.alias;
	}

	public String toSqlString() {
		StringBuilder sb = this.genSqlStringBuilder();
		sb.append(this.column).append(" ").append("IN").append(" ").append("(").append(":")
				.append(this.getColumnKeyName()).append(")");
		return sb.toString();
	}
}