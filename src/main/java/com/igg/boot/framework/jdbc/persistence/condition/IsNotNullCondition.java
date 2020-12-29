package com.igg.boot.framework.jdbc.persistence.condition;

import java.util.Collections;
import java.util.Map;

public class IsNotNullCondition extends TableNameCondition {
	private static final long serialVersionUID = -5803142368051988341L;
	private String column;

	public IsNotNullCondition() {
		super("");
	}

	public IsNotNullCondition(String column) {
		this(column, "");
	}

	public IsNotNullCondition(String column, String tableName) {
		super(tableName);
		this.column = column;
		parseColumn();
	}

	public String getColumn() {
		return this.column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Map<String, Object> getParameters() {
		return Collections.emptyMap();
	}

	public String toSqlString() {
		return this.genSqlStringBuilder().append(this.column).append(" ").append("IS NOT NULL").toString();
	}
}