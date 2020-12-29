package com.igg.boot.framework.jdbc.persistence.condition;

import java.util.HashMap;
import java.util.Map;

public class BetweenAndCondition extends TableNameCondition {
	public static final String BETWEEN_LOWER = "lower";
	public static final String BETWEEN_UPPER = "upper";
	private static final long serialVersionUID = -8402100982159870290L;
	private String column;
	private Object lowerLimit;
	private Object upperLimit;

	public BetweenAndCondition() {
		super("");
	}

	public BetweenAndCondition(String tableName) {
		super(tableName);
	}

	public BetweenAndCondition(String column, Object lowerLimit, Object upperLimit) {
		this(column, lowerLimit, upperLimit, "");
	}

	public BetweenAndCondition(String column, Object lowerLimit, Object upperLimit, String tableName) {
		super(tableName);
		this.column = column;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		parseColumn();
	}

	public Map<String, Object> getParameters() {
		Map<String, Object> parms = new HashMap<>();
		parms.put(this.getLower(), this.lowerLimit);
		parms.put(this.getUpper(), this.upperLimit);
		return parms;
	}

	public String getLower() {
		return (new StringBuilder(64)).append("lower").append("_").append(this.column).toString();
	}

	public String getUpper() {
		return (new StringBuilder(64)).append("upper").append("_").append(this.column).toString();
	}

	public String toSqlString() {
		StringBuilder sqlStr = this.genSqlStringBuilder();
		sqlStr.append(this.column).append(" ").append("BETWEEN").append(" ").append(":").append(this.getLower())
				.append(" ").append("AND").append(" ").append(":").append(this.getUpper());
		return sqlStr.toString();
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setLowerLimit(Object lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public void setUpperLimit(Object upperLimit) {
		this.upperLimit = upperLimit;
	}

	public String getColumn() {
		return this.column;
	}

	public Object getLowerLimit() {
		return this.lowerLimit;
	}

	public Object getUpperLimit() {
		return this.upperLimit;
	}
}