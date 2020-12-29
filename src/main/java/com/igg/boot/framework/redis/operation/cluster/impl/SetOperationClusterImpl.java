package com.igg.boot.framework.redis.operation.cluster.impl;

import java.util.Optional;
import java.util.Set;

import com.igg.boot.framework.redis.operation.SetOperation;

import redis.clients.jedis.JedisCluster;

public class SetOperationClusterImpl implements SetOperation{
	private JedisCluster jedisCluster;
	
	public SetOperationClusterImpl(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	
	@Override
	public void sadd(String key, String... values) {
		jedisCluster.sadd(key, values);
	}

	@Override
	public long scard(String key) {
		return jedisCluster.scard(key);
	}

	@Override
	public boolean sismember(String key, String value) {
		return jedisCluster.sismember(key, value);
	}

	@Override
	public Set<String> smembers(String key) {
		return jedisCluster.smembers(key);
	}

	@Override
	public long srem(String key, String... values) {
		return jedisCluster.srem(key, values);
	}

	@Override
	public Optional<String> spop(String key) {
		return Optional.ofNullable(jedisCluster.spop(key));
	}

    @Override
    public long zadd(String key, double score, String value) {
        // TODO Auto-generated method stub
        return jedisCluster.zadd(key, score, value);
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        return jedisCluster.zrange(key, start, end);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        return jedisCluster.zrangeByScore(key, min, max);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return jedisCluster.zrangeByScore(key, min, max, offset, count);
    }

    @Override
    public long zrank(String key, String value) {
        return jedisCluster.zrank(key, value);
    }

    @Override
    public long zrevrank(String key, String value) {
        return jedisCluster.zrevrank(key, value);
    }

    @Override
    public long zrem(String key, String... values) {
        return jedisCluster.zrem(key, values);
    }

    @Override
    public long zremrangeByScore(String key, double min, double max) {
        return jedisCluster.zremrangeByScore(key, min, max);
    }

    @Override
    public long zremrangeByRank(String key, long start, long end) {
        return jedisCluster.zremrangeByRank(key, start, end);
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        return jedisCluster.zrevrange(key, start, end);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return jedisCluster.zrevrangeByScore(key, max, min);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public long zcard(String key) {
        return jedisCluster.zcard(key);
    }

    @Override
    public long zcount(String key, double min, double max) {
        return jedisCluster.zcount(key, min, max);
    }

    @Override
    public double zscore(String key, String value) {
        return jedisCluster.zscore(key, value);
    }

    @Override
    public double zincrby(String key, double score, String member) {
        return jedisCluster.zincrby(key, score, member);
    }

}
