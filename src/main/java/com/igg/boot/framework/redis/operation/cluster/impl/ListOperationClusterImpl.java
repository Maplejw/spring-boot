package com.igg.boot.framework.redis.operation.cluster.impl;

import java.util.List;
import java.util.Optional;

import com.igg.boot.framework.redis.operation.ListOperation;

import redis.clients.jedis.JedisCluster;

public class ListOperationClusterImpl implements ListOperation {
	private JedisCluster jedisCluster;
	
	public ListOperationClusterImpl(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	
	@Override
	public Optional<String> blpop(String key, int timeout) {
		return Optional.ofNullable(jedisCluster.blpop(timeout, key).get(0));
	}

	@Override
	public Optional<String> brpop(String key, int timeout) {
		return Optional.ofNullable(jedisCluster.brpop(timeout, key).get(0));
	}

	@Override
	public long llen(String key) {
		return jedisCluster.llen(key);
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return jedisCluster.lrange(key, start, end);
	}

	@Override
	public void lset(String key, long index, String value) {
		jedisCluster.lset(key, index, value);
	}

	@Override
	public Optional<String> rpop(String key) {
		return Optional.ofNullable(jedisCluster.rpop(key));
	}

	@Override
	public void rpush(String key, String... value) {
		jedisCluster.rpush(key, value);
	}

	@Override
	public Optional<String> lpop(String key) {
		return Optional.ofNullable(jedisCluster.lpop(key));
	}

	@Override
	public void lpush(String key, String... value) {
		jedisCluster.lpush(key, value);
	}

	@Override
	public Optional<String> lindex(String key, long index) {
		return Optional.ofNullable(jedisCluster.lindex(key, index));
	}

}
