package com.igg.boot.framework.stragegy;

import org.springframework.stereotype.Component;

import com.igg.boot.framework.jdbc.persistence.shard.HashShardStrategy;
import com.igg.boot.framework.model.UserShardModel;

@Component
public class UserHashShardStrategy extends HashShardStrategy<UserShardModel> {
	public UserHashShardStrategy() {
		super(2, "_", "id");
	}

}
