package com.igg.boot.framework.redis;

import com.igg.boot.framework.redis.operation.cluster.impl.HashOperationClusterImpl;
import com.igg.boot.framework.redis.operation.cluster.impl.KeyOperationClusterImpl;
import com.igg.boot.framework.redis.operation.cluster.impl.ListOperationClusterImpl;
import com.igg.boot.framework.redis.operation.cluster.impl.SetOperationClusterImpl;
import com.igg.boot.framework.redis.operation.cluster.impl.StringOperationClusterImpl;

import redis.clients.jedis.JedisCluster;

public class JedisClusterTemplate extends JedisTemplate {

	public JedisClusterTemplate(JedisCluster jedisCluster) {
		super(new KeyOperationClusterImpl(jedisCluster), new StringOperationClusterImpl(jedisCluster),
				new HashOperationClusterImpl(jedisCluster), new ListOperationClusterImpl(jedisCluster),
				new SetOperationClusterImpl(jedisCluster));
	}

}
