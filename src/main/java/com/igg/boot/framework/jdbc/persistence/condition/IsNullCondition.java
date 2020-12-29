package com.igg.boot.framework.jdbc.persistence.condition;

import java.util.HashMap;
import java.util.Map;

public class IsNullCondition extends TableNameCondition {
	private static final long serialVersionUID = 5041004211231977803L;
	private String column;

	public IsNullCondition() {
		super("");
	}

	public IsNullCondition(String tableName) {
		this(tableName, "");
	}

	public IsNullCondition(String column, String tableName) {
		super(tableName);
		this.column = column;
		parseColumn();
	}

	public Map<String, Object> getParameters() {
		return new HashMap<>();
	}

	public String toSqlString() {
		return this.genSqlStringBuilder().append(this.column).append(" ").append("IS NULL").toString();
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getColumn() {
		return this.column;
	}
}