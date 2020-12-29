package com.igg.boot.framework.redis;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.igg.boot.framework.redis.operation.HashOperation;
import com.igg.boot.framework.redis.operation.KeyOperation;
import com.igg.boot.framework.redis.operation.ListOperation;
import com.igg.boot.framework.redis.operation.SetOperation;
import com.igg.boot.framework.redis.operation.StringOperation;

public class JedisTemplate implements HashOperation, StringOperation, KeyOperation, ListOperation, SetOperation {
	private KeyOperation keyOperation;
	private StringOperation stringOperation;
	private HashOperation hashOperation;
	private ListOperation listOperation;
	private SetOperation setOperation;
	
	public JedisTemplate(KeyOperation keyOperation,StringOperation stringOperation,HashOperation hashOperation,ListOperation listOperation,SetOperation setOperation) {
		this.keyOperation = keyOperation;
		this.stringOperation = stringOperation;
		this.hashOperation = hashOperation;
		this.listOperation = listOperation;
		this.setOperation = setOperation;
	}
	
	public Optional<String> get(String key) {
		return this.stringOperation.get(key);
	}

	public void set(String key, String value) {
		this.stringOperation.set(key, value);
	}

	public void del(String key) {
		this.keyOperation.del(key);
	}

	public long incr(String key) {
		return this.stringOperation.incr(key);
	}

	public long incrBy(String key, int increment) {
		return this.stringOperation.incrBy(key, increment);
	}

	public long decr(String key) {
		return this.stringOperation.decr(key);
	}

	public long decrby(String key, int increment) {
		return this.stringOperation.decrBy(key, increment);
	}

	public boolean exists(String key) {
		return this.keyOperation.exists(key);
	}

	@Override
	public void hdel(String key, String... fields) {
		this.hashOperation.hdel(key, fields);
	}

	@Override
	public boolean hexists(String key, String field) {
		return this.hashOperation.hexists(key, field);
	}

	@Override
	public Optional<String> hget(String key, String field) {
		return this.hashOperation.hget(key, field);
	}

	@Override
	public Map<String, String> hgetall(String key) {
		return this.hashOperation.hgetall(key);
	}

	@Override
	public long hincrby(String key, String field, long increment) {
		return this.hashOperation.hincrby(key, field, increment);
	}

	@Override
	public Set<String> hkeys(String key) {
		return this.hashOperation.hkeys(key);
	}

	@Override
	public long hlen(String key) {
		return this.hashOperation.hlen(key);
	}

	@Override
	public void hset(String key, String field, String value) {
		this.hashOperation.hset(key, field, value);
	}

	@Override
	public long hsetnx(String key, String field, String value) {
		return this.hashOperation.hsetnx(key, field, value);
	}

	@Override
	public List<String> hvals(String key) {
		return this.hashOperation.hvals(key);
	}

	@Override
	public void hmset(String key, Map<String, String> values) {
		this.hashOperation.hmset(key, values);
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		return this.hashOperation.hmget(key, fields);
	}

	@Override
	public long decrBy(String key, int increment) {
		return this.stringOperation.decrBy(key, increment);
	}

	@Override
	public Optional<String> getSet(String key, String value) {
		return this.stringOperation.getSet(key, value);
	}

	@Override
	public void setex(String key, String value, int expired) {
		this.stringOperation.setex(key, value, expired);
	}

	@Override
	public boolean setexnx(String key, String value, int expired) {
		return this.stringOperation.setexnx(key, value, expired);
	}

	@Override
	public void append(String key, String value) {
		this.stringOperation.append(key, value);
	}

	@Override
	public void setbit(String key, long offset, String value) {
		this.stringOperation.setbit(key, offset, value);
	}

	@Override
	public void expire(String key, int seconds) {
		this.keyOperation.expire(key, seconds);
	}

	@Override
	public Optional<String> blpop(String key, int timeout) {
		return this.listOperation.blpop(key, timeout);
	}

	@Override
	public Optional<String> rpop(String key) {
		return this.listOperation.rpop(key);
	}

	@Override
	public Optional<String> lpop(String key) {
		return this.listOperation.lpop(key);
	}

	@Override
	public Optional<String> brpop(String key, int timeout) {
		return this.listOperation.brpop(key, timeout);
	}

	@Override
	public long llen(String key) {
		return this.listOperation.llen(key);
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return this.listOperation.lrange(key, start, end);
	}

	@Override
	public void lset(String key, long index, String value) {
		this.listOperation.lset(key, index, value);
	}

	@Override
	public void rpush(String key, String... value) {
		this.listOperation.rpush(key, value);
	}

	@Override
	public void lpush(String key, String... value) {
		this.listOperation.lpush(key, value);
	}

	@Override
	public Optional<String> lindex(String key, long index) {
		return this.listOperation.lindex(key, index);
	}

	@Override
	public void sadd(String key, String... values) {
		this.setOperation.sadd(key, values);
	}

	@Override
	public long scard(String key) {
		return this.setOperation.scard(key);
	}

	@Override
	public boolean sismember(String key, String value) {
		return this.setOperation.sismember(key, value);
	}

	@Override
	public Set<String> smembers(String key) {
		return this.setOperation.smembers(key);
	}

	@Override
	public long srem(String key, String... values) {
		return this.setOperation.srem(key, values);
	}

	@Override
	public Optional<String> spop(String key) {
		return this.setOperation.spop(key);
	}

    @Override
    public long zadd(String key, double score, String value) {
        // TODO Auto-generated method stub
        return this.setOperation.zadd(key, score, value);
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        return this.setOperation.zrange(key, start, end);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        return this.setOperation.zrangeByScore(key, min, max);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        // TODO Auto-generated method stub
        return this.setOperation.zrangeByScore(key, min, max, offset, count);
    }

    @Override
    public long zrank(String key, String value) {
        // TODO Auto-generated method stub
        return this.setOperation.zrank(key, value);
    }

    @Override
    public long zrevrank(String key, String value) {
        // TODO Auto-generated method stub
        return this.setOperation.zrevrank(key, value);
    }

    @Override
    public long zrem(String key, String... values) {
        // TODO Auto-generated method stub
        return this.setOperation.zrem(key, values);
    }

    @Override
    public long zremrangeByScore(String key, double min, double max) {
        // TODO Auto-generated method stub
        return this.setOperation.zremrangeByScore(key, min, max);
    }

    @Override
    public long zremrangeByRank(String key, long start, long end) {
        // TODO Auto-generated method stub
        return this.setOperation.zremrangeByRank(key, start, end);
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        // TODO Auto-generated method stub
        return this.setOperation.zrevrange(key, start, end);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        // TODO Auto-generated method stub
        return this.setOperation.zrevrangeByScore(key, max, min);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        // TODO Auto-generated method stub
        return this.setOperation.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public long zcard(String key) {
        // TODO Auto-generated method stub
        return this.setOperation.zcard(key);
    }

    @Override
    public long zcount(String key, double min, double max) {
        // TODO Auto-generated method stub
        return this.setOperation.zcount(key, min, max);
    }

    @Override
    public double zscore(String key, String value) {
        // TODO Auto-generated method stub
        return this.setOperation.zscore(key, value);
    }

    @Override
    public double zincrby(String key, double score, String member) {
        // TODO Auto-generated method stub
        return this.setOperation.zincrby(key, score, member);
    }
	
}
