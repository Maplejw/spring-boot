package com.igg.boot.framework.redis.operation.cluster.impl;

import java.util.Optional;

import com.igg.boot.framework.redis.RedisAutoconfiguration;
import com.igg.boot.framework.redis.operation.StringOperation;

import redis.clients.jedis.JedisCluster;

public class StringOperationClusterImpl implements StringOperation {
	private JedisCluster jedisCluster;

	public StringOperationClusterImpl(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	
	@Override
	public Optional<String> get(String key) {
		String data = jedisCluster.get(key);

		return Optional.ofNullable(data);
	}

	@Override
	public void set(String key, String value) {
		jedisCluster.set(key, value);
	}
	
	@Override
	public long incr(String key) {
		return jedisCluster.incr(key);
	}

	@Override
	public long incrBy(String key, int increment) {
		return jedisCluster.incrBy(key, increment);
	}

	@Override
	public long decr(String key) {
		return jedisCluster.decr(key);
	}

	@Override
	public long decrBy(String key, int increment) {
		return jedisCluster.decrBy(key, increment);
	}
	
	@Override
	public Optional<String> getSet(String key, String value) {
		String data = jedisCluster.getSet(key, value);

		return Optional.ofNullable(data);
	}

	@Override
	public void setex(String key, String value, int expired) {
		jedisCluster.setex(key, expired, value);
	}

	@Override
	public boolean setexnx(String key, String value, int expired) {
		String code = jedisCluster.set(key, value, RedisAutoconfiguration.NX, RedisAutoconfiguration.EX, expired);
		
		return code != null && code.equals("OK");
	}

	@Override
	public void append(String key, String value) {
		jedisCluster.append(key, value);
	}

	@Override
	public void setbit(String key, long offset, String value) {
		jedisCluster.setbit(key, offset, value);
	}

}
