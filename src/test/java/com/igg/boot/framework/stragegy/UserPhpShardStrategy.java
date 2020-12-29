package com.igg.boot.framework.stragegy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.igg.boot.framework.jdbc.persistence.shard.AbstractShardStrategy;
import com.igg.boot.framework.model.UserPhpModel;

@Component
public class UserPhpShardStrategy extends AbstractShardStrategy<UserPhpModel> {

	public UserPhpShardStrategy() {
		super(2, "_", "username");
	}

	@Override
	protected int getShard(Object value) {
		return (int) ((long)value % factor);
	}

	@Override
	public String getShardName(Map<String, Object> params) {
		String value = (String) params.get(field);
		long hash = getHash(value);
		
		return split + getShard(hash);
	}

	private long  getHash(String index) {
		String s = index.toLowerCase();
		long hash = 0;
		for (int i = 0; i < s.length(); i++) {
			int tmp = (int) s.charAt(i);
			hash = uint32(hash * 397 + tmp);
		}

		return hash;
	}

	private long uint32(long data) {
		if (0 > data || data > 4294967295L) {
			data &= 4294967295L;
			if (0 > data) {
				data = -data;
			}
		}

		return data;
	}

}
