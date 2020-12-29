package com.igg.boot.framework.redis.operation.cluster.impl;

import com.igg.boot.framework.redis.operation.KeyOperation;

import redis.clients.jedis.JedisCluster;

public class KeyOperationClusterImpl implements KeyOperation{
	private JedisCluster jedisCluster;
	
	public KeyOperationClusterImpl(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	
	@Override
	public void del(String key) {
		jedisCluster.del(key);
	}

	@Override
	public boolean exists(String key) {
		return jedisCluster.exists(key);
	}

	@Override
	public void expire(String key, int seconds) {
		this.jedisCluster.expire(key, seconds);
	}

}
