package com.igg.boot.framework.dao;

import org.springframework.stereotype.Repository;

import com.igg.boot.framework.jdbc.persistence.dao.impl.BaseDaoImpl;
import com.igg.boot.framework.model.UserPhpModel;

@Repository
public class UserPHPDao extends BaseDaoImpl<UserPhpModel, Long> {
}
