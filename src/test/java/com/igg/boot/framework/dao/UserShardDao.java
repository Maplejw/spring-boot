package com.igg.boot.framework.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.igg.boot.framework.jdbc.persistence.dao.impl.BaseDaoImpl;
import com.igg.boot.framework.model.UserShardModel;

@Repository
public class UserShardDao extends BaseDaoImpl<UserShardModel, Long> {
	public Optional<UserShardModel> getUserById(long id) {
		return unique(id);
	}
}
