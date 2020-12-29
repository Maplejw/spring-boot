package com.igg.boot.framework.jdbc.persistence.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.igg.boot.framework.jdbc.persistence.Entity;
import com.igg.boot.framework.jdbc.persistence.bean.DataStore;
import com.igg.boot.framework.jdbc.persistence.bean.JoinItem;
import com.igg.boot.framework.jdbc.persistence.bean.PagingParameter;
import com.igg.boot.framework.jdbc.persistence.condition.Condition;
import com.igg.boot.framework.jdbc.persistence.exception.DuplicateRecordException;

public interface BaseDao<E extends Entity, K extends Number> {
	int count();

	int count(Condition arg0);

	int count(String arg0, Map<String, Object> arg1);

	int delete(K arg0);

	int deleteCondition(Condition arg0);

	int deletes(List<K> arg0);

	int executeChangeSql(String arg0, Map<String, ?> arg1);

	Optional<E> unique(K arg0);

	Optional<E> unique(String arg0, Map<String, Object> arg1) throws DuplicateRecordException;

	Optional<E> unique(Condition arg0) throws DuplicateRecordException;

	List<E> query(String arg0, Map<String, Object> arg1);

	List<E> query(Condition arg0);

	List<E> query(Condition arg0, String arg1);

	List<E> query(String arg0, PagingParameter arg1, Map<String, Object> arg2);

	List<E> query(Condition arg0, PagingParameter arg1);

	List<E> query(Condition arg0, String arg1, PagingParameter arg2);

	int save(E arg0);

	int save(E arg0, K arg1);

	int saves(List<E> arg0);

	int saves(List<E> arg0, K[] arg1);

	int saveOrUpdate(E arg0);

	List<Map<String, Object>> search(String arg0, Map<String, Object> arg1);

	List<Map<String, Object>> search(String arg0, PagingParameter arg1, Map<String, Object> arg2);

	int update(E arg0);

	int update(String arg0, Map<String, Object> arg1);

	List<Map<String, Object>> join(Condition arg0, JoinItem... arg1);

	List<Map<String, Object>> join(Condition arg0, String arg1, JoinItem... arg2);

	<R> List<R> join(Condition arg0, RowMapper<R> arg1, JoinItem... arg2);

	<R> List<R> join(Condition arg0, String arg1, RowMapper<R> arg2, JoinItem... arg3);

	DataStore<Map<String, Object>> join(Condition arg0, PagingParameter arg1, JoinItem... arg2);

	DataStore<Map<String, Object>> join(Condition arg0, String arg1, PagingParameter arg2, JoinItem... arg3);

	<R> DataStore<R> join(Condition arg0, RowMapper<R> arg1, PagingParameter arg2, JoinItem... arg3);

	<R> DataStore<R> join(Condition arg0, String arg1, RowMapper<R> arg2, PagingParameter arg3, JoinItem... arg4);

	<R> R join(Condition arg0, String arg1, ResultSetExtractor<R> arg2, JoinItem... arg3);

	<R> R join(Condition arg0, ResultSetExtractor<R> arg1, JoinItem... arg2);

	<R> DataStore<R> join(Condition arg0, ResultSetExtractor<List<R>> arg1, PagingParameter arg2, JoinItem... arg3);

	<R> DataStore<R> join(Condition arg0, String arg1, ResultSetExtractor<List<R>> arg2, PagingParameter arg3,
			JoinItem... arg4);
}