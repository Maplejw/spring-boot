package com.igg.boot.framework.jdbc.persistence.shard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.igg.boot.framework.jdbc.persistence.Entity;

import lombok.Data;

@Data
public class ShardStrategyMap {
	private Map<Class<? extends Entity>, ShardStrategy<? extends Entity>> shardStrategyMap;

	public ShardStrategyMap(List<ShardStrategy<? extends Entity>> shardStrategyList) {
		shardStrategyMap = new HashMap<>(shardStrategyList.size());
		shardStrategyList.forEach((shardStrategy) -> {
			shardStrategyMap.put(shardStrategy.getEntityClass(), shardStrategy);
		});

	}
}
