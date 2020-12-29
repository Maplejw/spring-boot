package com.igg.boot.framework.jdbc.persistence.condition;

public abstract class NoTableNameCondition extends Condition {
	private static final long serialVersionUID = -2046584799972445200L;

	public String getTableName() {
		return "";
	}

	public Condition setTableName(String tableName) {
		throw new RuntimeException(this.getClass().getName() + "不支持添加表名!");
	}
}