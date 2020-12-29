package com.igg.boot.framework.redis.operation.single.impl;

import java.util.List;
import java.util.Optional;

import com.igg.boot.framework.redis.RedisExecute;
import com.igg.boot.framework.redis.operation.ListOperation;

public class ListOperationImpl implements ListOperation {
	private RedisExecute redisExecute;
	
	public ListOperationImpl(RedisExecute redisExecute) {
		this.redisExecute = redisExecute;
	}
	
	@Override
	public Optional<String> blpop(String key, int timeout) {
		return Optional.ofNullable(redisExecute.execute(jedis -> jedis.blpop(timeout, key)).get(0));
	}

	@Override
	public Optional<String> rpop(String key) {
		return Optional.ofNullable(redisExecute.execute(jedis -> jedis.rpop(key)));
	}

	@Override
	public Optional<String> lpop(String key) {
		return Optional.ofNullable(redisExecute.execute(jedis -> jedis.lpop(key)));
	}

	@Override
	public Optional<String> brpop(String key, int timeout) {
		return Optional.ofNullable(redisExecute.execute(jedis -> jedis.brpop(timeout, key)).get(0));
	}

	@Override
	public long llen(String key) {
		return redisExecute.execute(jedis -> jedis.llen(key));
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return redisExecute.execute(jedis -> jedis.lrange(key, start, end));
	}

	@Override
	public void lset(String key, long index, String value) {
		redisExecute.execute(jedis -> jedis.lset(key, index, value));
	}

	@Override
	public void rpush(String key, String... value) {
		redisExecute.execute(jedis -> jedis.rpush(key, value));
	}

	@Override
	public void lpush(String key, String... value) {
		redisExecute.execute(jedis -> jedis.lpush(key, value));
	}

	@Override
	public Optional<String> lindex(String key, long index) {
		return Optional.ofNullable(redisExecute.execute(jedis -> jedis.lindex(key, index)));
	}

}
