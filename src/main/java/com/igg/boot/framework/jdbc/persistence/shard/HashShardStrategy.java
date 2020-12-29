package com.igg.boot.framework.jdbc.persistence.shard;

import com.igg.boot.framework.jdbc.persistence.Entity;

public class HashShardStrategy<E extends Entity> extends AbstractShardStrategy<E> {
	
	public HashShardStrategy(int factor,String split,String field) {
		super(factor, split,field);
	}

	@Override
	protected int getShard(Object value) {
		return (int) ((long)value % factor);
	}
	
}
