package com.igg.boot.framework.redis.operation.single.impl;

import com.igg.boot.framework.redis.RedisExecute;
import com.igg.boot.framework.redis.operation.KeyOperation;

public class KeyOperationImpl implements KeyOperation {
	private RedisExecute redisExecute;

	public KeyOperationImpl(RedisExecute redisExecute) {
		this.redisExecute = redisExecute;
	}

	@Override
	public void del(String key) {
		redisExecute.execute(jedis -> jedis.del(key));
	}

	@Override
	public boolean exists(String key) {
		return redisExecute.execute(jedis -> jedis.exists(key));
	}

	@Override
	public void expire(String key, int seconds) {
		redisExecute.execute(jedis -> jedis.expire(key, seconds));
	}

}
