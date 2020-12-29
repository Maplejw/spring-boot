package com.igg.boot.framework.jdbc.persistence.condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

public class NotInCondition extends TableNameCondition {
	private static final long serialVersionUID = -313075395842837260L;
	private String column;
	private List<Object> parameters;
	private String alias;

	public NotInCondition() {
		super("");
	}

	public NotInCondition(String column, List<Object> parameters) {
		this("", column, parameters);
	}

	public NotInCondition(String tableName, String column, List<Object> parameters) {
		super(tableName);
		this.column = column;
		this.parameters = parameters;
	}

	public NotInCondition(String column, List<Object> parameters, String alias) {
		this("", column, parameters, alias);
	}

	public NotInCondition(String tableName, String column, List<Object> parameters, String alias) {
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
		sb.append(this.column).append(" ").append("NOT IN").append(" ").append("(").append(":")
				.append(this.getColumnKeyName()).append(")");
		return sb.toString();
	}
}