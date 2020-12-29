package com.igg.boot.framework.redis.operation.single.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.igg.boot.framework.redis.RedisExecute;
import com.igg.boot.framework.redis.operation.HashOperation;

public class HashOperationImpl implements HashOperation {
	private RedisExecute redisExecute;
	
	public HashOperationImpl(RedisExecute redisExecute) {
		this.redisExecute = redisExecute;
	}
	
	@Override
	public void hdel(String key, String... fields) {
		redisExecute.execute(jedis -> jedis.hdel(key, fields));
	}

	@Override
	public boolean hexists(String key, String field) {
		return redisExecute.execute(jedis -> jedis.hexists(key, field));
	}

	@Override
	public Optional<String> hget(String key, String field) {
		return Optional.ofNullable(redisExecute.execute(jedis -> jedis.hget(key, field)));
	}

	@Override
	public Map<String, String> hgetall(String key) {
		return redisExecute.execute(jedis -> jedis.hgetAll(key));
	}

	@Override
	public long hincrby(String key, String field, long increment) {
		return redisExecute.execute(jedis -> jedis.hincrBy(key, field, increment));
	}

	@Override
	public Set<String> hkeys(String key) {
		return redisExecute.execute(jedis -> jedis.hkeys(key));
	}

	@Override
	public long hlen(String key) {
		return redisExecute.execute(jedis -> jedis.hlen(key));
	}

	@Override
	public void hset(String key, String field, String value) {
		redisExecute.execute(jedis -> jedis.hset(key, field, value));
	}

	@Override
	public long hsetnx(String key, String field, String value) {
		return redisExecute.execute(jedis -> jedis.hsetnx(key, field, value));
	}

	@Override
	public List<String> hvals(String key) {
		return redisExecute.execute(jedis -> jedis.hvals(key));
	}

	@Override
	public void hmset(String key, Map<String, String> values) {
		redisExecute.execute(jedis -> jedis.hmset(key, values));
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		return redisExecute.execute(jedis -> jedis.hmget(key, fields));
	}

}
