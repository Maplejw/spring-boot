package com.igg.boot.framework.redis;

import redis.clients.jedis.Jedis;

public interface RedisExecuteCommand<T> {
	T executeCommand(Jedis jedis);
}
