package com.igg.boot.framework.jdbc.persistence.condition;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.igg.boot.framework.jdbc.persistence.Entity;

public class SimpleCondition extends TableNameCondition {
	private static final long serialVersionUID = -2874189715688238636L;
	private String column;
	private String operator;
	private Object parameter;
	private String alias;

	public SimpleCondition(String column, String operator, Object parameter,Class<? extends Entity> clz) {
		super(clz);
		this.column = column;
		this.operator = operator;
		this.parameter = parameter;
		this.alias = "";
		parseColumn();
		
	}
	
	public SimpleCondition(String column, String operator, Object parameter) {
		this(column, operator, parameter, "");
	}

	public SimpleCondition(String column, String operator, Object parameter, String alias) {
		this(column, operator, parameter, alias, "");
	}
	
	public SimpleCondition(String column, String operator, Object parameter, String alias, String tableName) {
		super(tableName);
		this.column = column;
		this.operator = operator;
		this.parameter = parameter;
		this.alias = alias;
		parseColumn();
	}
	
	public Map<String, Object> getParameters() {
		Map<String, Object> parms = new HashMap<>();
		parms.put(this.getColumnKeyName(), this.parameter);
		return parms;
	}

	private String getColumnKeyName() {
		return StringUtils.isEmpty(this.alias) ? this.column : this.alias;
	}

	public String toSqlString() {
		StringBuilder sb = this.genSqlStringBuilder();
		sb.append("`").append(this.column).append("`").append(" ").append(this.operator).append(" ").append(':');
		sb.append(this.getColumnKeyName());
		return sb.toString();
	}

	public String getColumn() {
		return this.column;
	}

	public String getOperator() {
		return this.operator;
	}

	public Object getParameter() {
		return this.parameter;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}