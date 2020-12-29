package com.igg.boot.framework.jdbc.persistence.condition;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.igg.boot.framework.jdbc.persistence.Entity;

public abstract class Condition implements Cloneable, Serializable {
	private static final long serialVersionUID = 4558142209318243533L;
	public static final String SPACE = " ";
	public static final String LEFT_BRACE = "(";
	public static final String RIGHT_BRACE = ")";
	public static final String BACK_QUOTE = "`";
	public static final String DIT = ".";
	public static final String EQ = "=";
	public static final String LT = "<";
	public static final String GT = ">";
	public static final String NE = "<>";
	public static final String LE = "<=";
	public static final String GE = ">=";
	public static final String LIKE = "LIKE";
	public static final String IS_NULL = "IS NULL";
	public static final String IS_NOT_NULL = "IS NOT NULL";
	public static final String BETWEEN = "BETWEEN";
	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String IN = "IN";
	public static final String NOT_IN = "NOT IN";
	public static final Condition ALWAYS_TRUE_CONDITION = new AlwaysTrueCondition();
	public static final Condition ALWAYS_FALSE_CONDITION = new AlwaysFalseCondition();

	public abstract Map<String, Object> getParameters();

	public abstract String toSqlString();

	public abstract String getTableName();

	public abstract Condition setTableName(String arg0);

	public Condition add(Condition component) {
		throw new RuntimeException("Add component not supported!");
	}
	
	public static Condition eq(String column, Object parameter) {
		return new SimpleCondition(column, "=", parameter);
	}
	
	public static Condition eq(String column, Object parameter, Class<? extends Entity> clz) {
		return new SimpleCondition(column, "=", parameter,clz);
	}

	public static Condition eq(String column, Object parameter, String alias) {
		return new SimpleCondition(column, "=", parameter, alias);
	}
	
	public static Condition lt(String column, Object parameter) {
		return new SimpleCondition(column, "<", parameter);
	}

	public static Condition lt(String column, Object parameter, String alias) {
		return new SimpleCondition(column, "<", parameter, alias);
	}

	public static Condition gt(String column, Object parameter) {
		return new SimpleCondition(column, ">", parameter);
	}

	public static Condition gt(String column, Object parameter, String alias) {
		return new SimpleCondition(column, ">", parameter, alias);
	}

	public static Condition ne(String column, Object parameter) {
		return new SimpleCondition(column, "<>", parameter);
	}

	public static Condition ne(String column, Object parameter, String alias) {
		return new SimpleCondition(column, "<>", parameter, alias);
	}

	public static Condition le(String column, Object parameter) {
		return new SimpleCondition(column, "<=", parameter);
	}

	public static Condition le(String column, Object parameter, String alias) {
		return new SimpleCondition(column, "<=", parameter, alias);
	}

	public static Condition ge(String column, Object parameter) {
		return new SimpleCondition(column, ">=", parameter);
	}

	public static Condition ge(String column, Object parameter, String alias) {
		return new SimpleCondition(column, ">=", parameter, alias);
	}

	public static Condition like(String column, String parameter) {
		return new SimpleCondition(column, "LIKE", parameter);
	}

	public static Condition like(String column, String parameter, String alias) {
		return new SimpleCondition(column, "LIKE", parameter, alias);
	}

	public static Condition isNull(String column) {
		return new IsNullCondition(column);
	}

	public static Condition isNotNull(String column) {
		return new IsNotNullCondition(column);
	}

	public static Condition betweenAnd(String column, Object lowerLimit, Object upperLimit) {
		return new BetweenAndCondition(column, lowerLimit, upperLimit);
	}

	public static Condition in(String column, List<Object> parameters) {
		return new InCondition(column, parameters);
	}

	public static Condition in(String column, List<Object> parameters, String alias) {
		return new InCondition(column, parameters, alias);
	}

	public static Condition notIn(String column, List<Object> parameters) {
		return new NotInCondition(column, parameters);
	}

	public static Condition notIn(String column, List<Object> parameters, String alias) {
		return new NotInCondition(column, parameters, alias);
	}

	public static Condition and(Condition... components) {
		return new AndCondition(components);
	}

	public static Condition or(Condition... components) {
		return new OrCondition(components);
	}
}