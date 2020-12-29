package com.igg.boot.framework.jdbc.persistence.shard;

import java.util.Map;

import com.igg.boot.framework.jdbc.persistence.Entity;

public interface ShardStrategy<E extends Entity> {
	 String getShardName(Map<String,Object> params);
	 
	 Class<E> getEntityClass();
	 
}
