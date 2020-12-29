package com.igg.boot.framework.stragegy;

import org.springframework.stereotype.Component;

import com.igg.boot.framework.jdbc.persistence.shard.HashShardStrategy;
import com.igg.boot.framework.model.UserOthersModel;

@Component
public class UserOthersShardStrategy extends HashShardStrategy<UserOthersModel> {
	public UserOthersShardStrategy() {
		super(2, "_", "id");
	}

}
