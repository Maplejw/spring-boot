package com.igg.boot.framework.redis.operation.single.impl;

import java.util.Optional;
import java.util.Set;

import com.igg.boot.framework.redis.RedisExecute;
import com.igg.boot.framework.redis.operation.SetOperation;
import com.igg.boot.framework.redis.operation.SetSingleOperation;

public class SetOperatiomImpl implements SetOperation, SetSingleOperation{
	private RedisExecute redisExecute;
	public SetOperatiomImpl(RedisExecute redisExecute) {
		this.redisExecute = redisExecute;
	}
	
	@Override
	public void sadd(String key, String... values) {
		redisExecute.execute(jedis -> jedis.sadd(key, values));
	}

	@Override
	public long scard(String key) {
		return redisExecute.execute(jedis -> jedis.scard(key));
	}

	@Override
	public boolean sismember(String key, String value) {
		return redisExecute.execute(jedis -> jedis.sismember(key, value));
	}

	@Override
	public Set<String> smembers(String key) {
		return redisExecute.execute(jedis -> jedis.smembers(key));
	}

	@Override
	public long srem(String key, String... values) {
		return redisExecute.execute(jedis -> jedis.srem(key, values));
	}

	@Override
	public Optional<String> spop(String key) {
		return Optional.ofNullable(redisExecute.execute(jedis -> jedis.spop(key)));
	}

	@Override
	public Set<String> sdiff(String... keys) {
		return redisExecute.execute(jedis -> jedis.sdiff(keys));
	}

	@Override
	public Set<String> sunion(String... keys) {
		return redisExecute.execute(jedis -> jedis.sunion(keys));
	}

	@Override
	public long sunionstore(String key, String... keys) {
		return redisExecute.execute(jedis -> jedis.sunionstore(key, keys));
	}

    @Override
    public long zadd(String key, double score, String value) {
        return redisExecute.execute(jedis -> jedis.zadd(key, score,value));
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        return redisExecute.execute(jedis -> jedis.zrange(key, start, end));
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        return redisExecute.execute(jedis -> jedis.zrangeByScore(key, min, max));
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return redisExecute.execute(jedis -> jedis.zrangeByScore(key, min, max,offset,count));
    }

    @Override
    public long zrank(String key, String value) {
        return redisExecute.execute(jedis -> jedis.zrank(key, value));
    }

    @Override
    public long zrevrank(String key, String value) {
        return redisExecute.execute(jedis -> jedis.zrevrank(key, value));
    }

    @Override
    public long zrem(String key, String... values) {
        return redisExecute.execute(jedis -> jedis.zrem(key, values));
    }

    @Override
    public long zremrangeByScore(String key, double min, double max) {
        return redisExecute.execute(jedis -> jedis.zremrangeByScore(key, min, max));
    }

    @Override
    public long zremrangeByRank(String key, long start, long end) {
        return redisExecute.execute(jedis -> jedis.zremrangeByRank(key, start, end));
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        return redisExecute.execute(jedis -> jedis.zrevrange(key, start, end));
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return redisExecute.execute(jedis -> jedis.zrevrangeByScore(key, max, min));
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return redisExecute.execute(jedis -> jedis.zrevrangeByScore(key, max, min, offset, count));
    }

    @Override
    public long zcard(String key) {
        return redisExecute.execute(jedis -> jedis.zcard(key));
    }

    @Override
    public long zcount(String key, double min, double max) {
        return redisExecute.execute(jedis -> jedis.zcount(key, min, max));
    }

    @Override
    public double zscore(String key, String value) {
        return redisExecute.execute(jedis -> jedis.zscore(key, value));
    }

    @Override
    public double zincrby(String key, double score, String member) {
        return redisExecute.execute(jedis -> jedis.zincrby(key, score, member));
    }

}
