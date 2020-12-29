package com.igg.boot.framework.redis;

import java.util.List;

import com.igg.boot.framework.redis.operation.StringSingleOperation;
import com.igg.boot.framework.redis.operation.single.impl.HashOperationImpl;
import com.igg.boot.framework.redis.operation.single.impl.KeyOperationImpl;
import com.igg.boot.framework.redis.operation.single.impl.ListOperationImpl;
import com.igg.boot.framework.redis.operation.single.impl.SetOperatiomImpl;
import com.igg.boot.framework.redis.operation.single.impl.StringOperationImpl;
import com.igg.boot.framework.redis.operation.single.impl.StringSingleOperationImpl;

public class JedisSingleTemplate extends JedisTemplate implements StringSingleOperation {
	private StringSingleOperation stringSingleOperation;
	
	public JedisSingleTemplate(RedisExecute redisExecute) {
		super(new KeyOperationImpl(redisExecute), new StringOperationImpl(redisExecute),
				new HashOperationImpl(redisExecute), new ListOperationImpl(redisExecute),
				new SetOperatiomImpl(redisExecute));
		this.stringSingleOperation = new StringSingleOperationImpl(redisExecute);
	}

	@Override
	public List<String> mget(String... keys) {
		return stringSingleOperation.mget(keys);
	}

	@Override
	public void mset(String... keysvalues) {
		stringSingleOperation.mset(keysvalues);
	}

}
