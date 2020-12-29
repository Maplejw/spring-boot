package com.igg.boot.framework.jdbc.persistence.condition;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.igg.boot.framework.autoconfigure.ApplicationContextHolder;
import com.igg.boot.framework.jdbc.persistence.Entity;
import com.igg.boot.framework.jdbc.persistence.shard.ShardStrategy;
import com.igg.boot.framework.jdbc.persistence.shard.ShardStrategyMap;

public abstract class TableNameCondition extends Condition {
	private static final long serialVersionUID = -626362262145097149L;
	private String tableName = "";
	private Class<? extends Entity> clz = null;

	TableNameCondition(String tableName) {
		this.tableName = tableName == null ? "" : tableName;
	}
	
	TableNameCondition(Class<? extends Entity> clz){
		this.clz = clz;
	}

	public Condition setTableName(String tableName) {
		this.tableName = tableName == null ? "" : tableName;
		return this;
	}
	
	public abstract String getColumn();
	
	public abstract void setColumn(String column);
	
	protected void parseColumn() {
		String column = getColumn();
		if(column.indexOf(Condition.DIT) > 0) {
			String[] columns = column.split("\\" + Condition.DIT);
			Assert.state(2 == columns.length, "列名表达式有误");
			setColumn(columns[1]);
			setTableName(columns[0]);
		}
	} 

	public String getTableName() {
		return this.tableName;
	}

	protected StringBuilder genSqlStringBuilder() {
		StringBuilder sqlStr = new StringBuilder(64);
		if (StringUtils.hasLength(this.getTableName()) && this.clz == null) {
			sqlStr.append("`").append(this.getTableName()).append("`").append(".");
		} else if(clz != null && StringUtils.hasLength(this.getTableName())) {
			ShardStrategyMap shardStrategyMap = (ShardStrategyMap) ApplicationContextHolder.getBean(ShardStrategyMap.class);
			ShardStrategy<? extends Entity> shardStrategy = shardStrategyMap.getShardStrategyMap().get(clz);
			if(shardStrategy != null) {
				sqlStr.append("`").append(this.tableName).append(shardStrategy.getShardName(getParameters())).append("`").append(".");
			}
		}
		
		return sqlStr;
	}
}