package com.igg.boot.framework.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.igg.boot.framework.jdbc.persistence.dao.impl.BaseDaoImpl;
import com.igg.boot.framework.model.UserModel;

@Repository
public class UserDao extends BaseDaoImpl<UserModel, Long> {
	public Optional<UserModel> getUserById(long id) {
		return unique(id);
	}
}
