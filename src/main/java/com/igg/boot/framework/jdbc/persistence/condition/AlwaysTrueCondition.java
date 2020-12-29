package com.igg.boot.framework.jdbc.persistence.condition;

import java.util.HashMap;
import java.util.Map;

public class AlwaysTrueCondition extends NoTableNameCondition {
	private static final long serialVersionUID = -6774936720410854164L;

	public Map<String, Object> getParameters() {
		return new HashMap<>();
	}

	public String toSqlString() {
		return "1 = 1";
	}
}