package com.igg.boot.framework.redis.operation.cluster.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.igg.boot.framework.redis.operation.HashOperation;

import redis.clients.jedis.JedisCluster;

public class HashOperationClusterImpl implements HashOperation{
	private JedisCluster jedisCluster;
	
	public HashOperationClusterImpl(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	
	@Override
	public void hdel(String key, String... fields) {
		jedisCluster.hdel(key, fields);
	}

	@Override
	public boolean hexists(String key, String field) {
		return jedisCluster.hexists(key, field);
	}

	@Override
	public Optional<String> hget(String key, String field) {
		return Optional.ofNullable(jedisCluster.hget(key, field));
	}

	@Override
	public Map<String, String> hgetall(String key) {
		return jedisCluster.hgetAll(key);
	}

	@Override
	public long hincrby(String key, String field, long increment) {
		return jedisCluster.hincrBy(key, field, increment);
	}

	@Override
	public Set<String> hkeys(String key) {
		return jedisCluster.hkeys(key);
	}

	@Override
	public long hlen(String key) {
		return jedisCluster.hlen(key);
	}

	@Override
	public void hset(String key, String field, String value) {
		jedisCluster.hset(key, field, value);
	}

	@Override
	public long hsetnx(String key, String field, String value) {
		return jedisCluster.hsetnx(key, field, value);
	}

	@Override
	public List<String> hvals(String key) {
		return jedisCluster.hvals(key);
	}

	@Override
	public void hmset(String key, Map<String, String> values) {
		jedisCluster.hmset(key, values);
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		return jedisCluster.hmget(key, fields);
	}

}
