package com.igg.boot.framework.redis.operation.single.impl;

import java.util.List;

import com.igg.boot.framework.redis.RedisExecute;
import com.igg.boot.framework.redis.operation.StringSingleOperation;

public class StringSingleOperationImpl implements StringSingleOperation {
	private RedisExecute redisExecute;
	
	public StringSingleOperationImpl(RedisExecute redisExecute) {
		this.redisExecute = redisExecute;
	}
	
	@Override
	public List<String> mget(String... keys) {
		return redisExecute.execute(jedis -> jedis.mget(keys));
	}

	@Override
	public void mset(String... keysvalues) {
		redisExecute.execute(jedis -> jedis.mset(keysvalues));
	}

}
