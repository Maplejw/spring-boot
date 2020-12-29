package com.igg.boot.framework.autoconfigure.es.condition;

import org.elasticsearch.index.query.QueryBuilder;

public abstract class Condition {
	public abstract QueryBuilder toQueryBuilder();
	
	public static TermCondition term(String key,Object value) {
		return new TermCondition(key,value);
	}
	
	public static RangeCondition range(String key) {
		return new RangeCondition(key);
	}
	
	public static AndCondition and(Condition condition) {
		return new AndCondition(condition);
	}
	
	public static ExitCondition exit(String key) {
	    return new ExitCondition(key);
	} 
	
	public static AndCondition and() {
		return new AndCondition("");
	}
	
	public static AndCondition and(String routing) {
        return new AndCondition(routing);
    }
}
