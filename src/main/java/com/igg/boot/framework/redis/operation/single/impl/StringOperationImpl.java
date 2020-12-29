package com.igg.boot.framework.redis.operation.single.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.util.Assert;

import com.igg.boot.framework.redis.RedisAutoconfiguration;
import com.igg.boot.framework.redis.RedisExecute;
import com.igg.boot.framework.redis.operation.StringOperation;
import com.igg.boot.framework.redis.operation.StringSingleOperation;

public class StringOperationImpl implements StringOperation, StringSingleOperation {
	private RedisExecute redisExecute;

	public StringOperationImpl(RedisExecute redisExecute) {
		this.redisExecute = redisExecute;
	}
	
	@Override
	public Optional<String> get(String key) {
		String data = redisExecute.execute((jedis) -> jedis.get(key));

		return Optional.ofNullable(data);
	}

	@Override
	public void set(String key, String value) {
		redisExecute.execute(jedis -> jedis.set(key, value));
	}

	@Override
	public long incr(String key) {
		return redisExecute.execute(jedis -> jedis.incr(key));
	}

	@Override
	public long incrBy(String key, int increment) {
		return redisExecute.execute(jedis -> jedis.incrBy(key, increment));
	}

	@Override
	public long decr(String key) {
		return redisExecute.execute(jedis -> jedis.decr(key));
	}

	@Override
	public long decrBy(String key, int increment) {
		return redisExecute.execute(jedis -> jedis.decrBy(key, increment));
	}
	
	@Override
	public Optional<String> getSet(String key, String value) {
		String data = redisExecute.execute(jedis -> jedis.getSet(key, value));
		
		return Optional.ofNullable(data);
	}

	@Override
	public void setex(String key, String value, int expired) {
		redisExecute.execute(jedis -> jedis.setex(key, expired, value));

	}

	@Override
	public boolean setexnx(String key, String value, int expired) {
		String code = redisExecute.execute(jedis -> jedis.set(key, value, RedisAutoconfiguration.NX, RedisAutoconfiguration.EX, expired));
		if(code == null) {
		    return false;
		}
		return code.equals("OK");
	}

	@Override
	public void append(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> mget(String... keys) {
		return redisExecute.execute(jedis -> jedis.mget(keys));
	}

	@Override
	public void setbit(String key, long offset, String value) {
		redisExecute.execute(jedis -> jedis.setbit(key, offset, value));
	}

	@Override
	public void mset(String... keysvalues) {
		Assert.isTrue(keysvalues.length / 2 == 0, "keysvalues 键值对有误");
		redisExecute.execute(jedis -> jedis.mset(keysvalues));
	}

}
