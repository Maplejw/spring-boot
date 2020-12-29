package com.igg.boot.framework.jdbc.persistence.condition;

import java.util.HashMap;
import java.util.Map;

public class AlwaysFalseCondition extends NoTableNameCondition {
	private static final long serialVersionUID = 1967347118278007459L;

	public Map<String, Object> getParameters() {
		return new HashMap<>();
	}

	public String toSqlString() {
		return "1 <> 1";
	}
}