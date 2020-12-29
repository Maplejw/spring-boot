package com.igg.boot.framework.jdbc.persistence.dao.impl;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;
import com.igg.boot.framework.jdbc.persistence.Entity;
import com.igg.boot.framework.jdbc.persistence.bean.DataStore;
import com.igg.boot.framework.jdbc.persistence.bean.JoinItem;
import com.igg.boot.framework.jdbc.persistence.bean.PagingParameter;
import com.igg.boot.framework.jdbc.persistence.builder.PagingSqlBuilder;
import com.igg.boot.framework.jdbc.persistence.builder.SimpleSqlBuilder;
import com.igg.boot.framework.jdbc.persistence.condition.Condition;
import com.igg.boot.framework.jdbc.persistence.dao.BaseDao;
import com.igg.boot.framework.jdbc.persistence.exception.DaoAccessException;
import com.igg.boot.framework.jdbc.persistence.exception.DuplicateRecordException;

public class BaseDaoImpl<E extends Entity, K extends Number> implements BaseDao<E, K> {
	private static final Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);
	private final SimpleSqlBuilder<E> simpleSqlBuilder;
	private final PagingSqlBuilder pagingSqlBuilder;
	private final Class<E> entityClass;
	@Autowired
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		ResolvableType resolvableType = ResolvableType.forClass(this.getClass());
		ResolvableType[] params = resolvableType.as(BaseDao.class).getGenerics();
		this.entityClass = (Class<E>) params[0].resolve();
		this.simpleSqlBuilder = SimpleSqlBuilder.fetchSimpleSqlBuilder(this.entityClass);
		this.pagingSqlBuilder = new PagingSqlBuilder();
		if (!this.simpleSqlBuilder.getIdField().getType().equals(params[1].resolve())) {
			throw new DaoAccessException("主键类型与模板类型不同");
		}
	}
	

	public int count() {
		String sql = this.simpleSqlBuilder.getQueryCountSql();
		
		return ((Integer) this.namedParameterJdbcTemplate.queryForObject(sql, new HashMap<>(), Integer.class)).intValue();
	}

	public int count(Condition condition) {
		Assert.notNull(condition, "查询条件不能为空");
		String sql = this.simpleSqlBuilder.getQueryCountSql(condition.getParameters()) + " WHERE " + condition.toSqlString();
		log.debug(sql);
		return ((Integer) this.namedParameterJdbcTemplate.queryForObject(sql, condition.getParameters(), Integer.class))
				.intValue();
	}

	public int count(String conditionSql, Map<String, Object> param) {
		String countSql = this.simpleSqlBuilder.getQueryCountSql(param) + " WHERE " + conditionSql;
		log.debug(conditionSql);
		return ((Integer) this.namedParameterJdbcTemplate.queryForObject(countSql, param, Integer.class)).intValue();
	}

	public int delete(K id) {
		Map<String,Object> params = new HashMap<>();
		params.put(this.simpleSqlBuilder.getIdFieldName(), id);
		return this.namedParameterJdbcTemplate.update(this.simpleSqlBuilder.getDeleteSql(id), params);
	}

	public int deleteCondition(Condition condition) {
		Objects.requireNonNull(condition, "用于进行删除的条件禁止为空");
		return this.namedParameterJdbcTemplate.update(
				this.simpleSqlBuilder.getDeleteContidionSql(condition), condition.getParameters());
	}

	public int deletes(List<K> ids) {
		String sql = this.simpleSqlBuilder.getDeleteInSql();
		Map<String,Object> parms = new HashMap<>();
		parms.put(this.simpleSqlBuilder.getIdFieldName(), ids);
		return this.namedParameterJdbcTemplate.update(sql, parms);
	}

	public int executeChangeSql(String sql, Map<String, ?> paramMap) {
		return this.namedParameterJdbcTemplate.update(sql, paramMap);
	}

	public Optional<E> unique(K id) {
		Map<String,Object> params = new HashMap<>();
		params.put(this.simpleSqlBuilder.getIdFieldName(), id);
		String sql = this.simpleSqlBuilder.getQuerySimpleSql(id);
		List<E> d = this.namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(params),
				new BeanPropertyRowMapper<E>(this.entityClass));
		return d.size() > 0 ? Optional.of(d.get(0)) : Optional.empty();
	}

	public Optional<E> unique(String sql, Map<String, Object> params) {
		return this.getUniqueEntity(this.query(sql, params));
	}

	private Optional<E> getUniqueEntity(List<E> entitys) {
		if (CollectionUtils.isEmpty(entitys)) {
			return Optional.empty();
		} else if (entitys.size() == 1) {
			return Optional.of(entitys.get(0));
		} else {
			throw new DuplicateRecordException("获取唯一记录时，记录超过了一个");
		}
	}

	public Optional<E> unique(Condition condition) throws DaoAccessException {
		return this.getUniqueEntity(this.query(condition));
	}

	public List<E> query(String sql, Map<String, Object> params) {
		return this.query((String) sql, (PagingParameter) null, params);
	}

	public List<E> query(String sql, PagingParameter paging, Map<String, Object> params) {
		sql = this.handleSimpleSql(sql,params);
		return this.queryDataStore(sql, params, paging);
	}

	private String handleSimpleSql(String sql, Map<String, Object> params) {
		if (StringUtils.isEmpty(sql)) {
			return this.simpleSqlBuilder.getQueryAllSql(params);
		} else {
			String checkSql = sql.trim().toUpperCase();
			if (!checkSql.startsWith("SELECT")) {
				if (!checkSql.startsWith("WHERE") && !checkSql.startsWith("ORDER")) {
					sql = "WHERE " + sql;
				}

				sql = this.simpleSqlBuilder.getQueryAllSql(params) + " " + sql;
			}

			log.debug(sql);
			return sql;
		}
	}

	private List<E> queryDataStore(String sql, Map<String, ?> params, PagingParameter paging) {
		if (paging != null && !paging.isInvalid()) {
			sql = this.pagingSqlBuilder.getPagingSql(sql, paging);
		}

		BeanPropertyRowMapper<E> rowMapper = new BeanPropertyRowMapper<>(this.entityClass);
		return this.namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(params), rowMapper);
	}

	public List<E> query(Condition condition) {
		return this.query((Condition) condition, (String) null, (PagingParameter) null);
	}

	public List<E> query(Condition condition, String orders) {
		return this.query((Condition) condition, (String) orders, (PagingParameter) null);
	}

	public List<E> query(Condition condition, PagingParameter paging) {
		return this.query((Condition) condition, (String) null, (PagingParameter) paging);
	}

	public List<E> query(Condition condition, String orders, PagingParameter paging) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder(64);
		if (condition != null) {
			params = condition.getParameters();
			sql.append(condition.toSqlString());
		}

		if (!StringUtils.isEmpty(orders)) {
			sql.append(" ORDER BY ").append(orders);
		}

		return this.query((String) sql.toString(), (PagingParameter) paging, (Map<String, Object>) params);
	}

	public int save(E entity) {
		return this.save(entity, null);
	}

	@SuppressWarnings("unchecked")
	public int save(E entity, K id) {
		if (!entity.isTransient()) {
			throw new DuplicateRecordException("The record " + entity + " is exist!");
		} else {
			Map<String, Object> params = this.simpleSqlBuilder.getSqlParameters(entity);
			String sql;
			int rows1;
			if (id == null) {
				sql = this.simpleSqlBuilder.getInsertSql(params);
				GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
				rows1 = this.namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);
				id = (K) keyHolder.getKey();
			} else {
				params.put(this.simpleSqlBuilder.getIdFieldName(), id);
				sql = this.simpleSqlBuilder.getIncludeIdFieldInsertSql(params,id);
				rows1 = this.namedParameterJdbcTemplate.update(sql, params);
			}
			this.setEntityId(entity, id);
			return rows1;
		}
	}

	private void setEntityId(E entity, K id) {
		Field field = this.simpleSqlBuilder.getIdField();
		field.setAccessible(true);

		try {
			field.set(entity, id);
		} catch (IllegalAccessException | IllegalArgumentException arg4) {
			throw new DaoAccessException("设置变量的主键时，类型不匹配", arg4);
		}
	}

	public int saves(List<E> entities) {
		return this.saves(entities, null);
	}

	public int saves(List<E> entities, K[] ids) {
		if (CollectionUtils.isEmpty(entities)) {
			return 0;
		} else {
			boolean hasIdTag = ids != null && ids.length > 0;
			if (hasIdTag && ids.length != entities.size()) {
				throw new HttpSystemException(HttpSystemExceptionCode.METHOD_PARAM_ERROR, "主键总数与实体总数不同");
			} else {
				int entitySize = entities.size();
				Map<String,Object> paramMap = new HashMap<>(entitySize * 10);

				for (int saveSql = 0; saveSql < entitySize; ++saveSql) {
					E entity = (E) entities.get(saveSql);
					if (!entity.isTransient()) {
						throw new DuplicateRecordException("The record " + entity + " is exist!");
					}

					if (hasIdTag) {
						paramMap.put(this.simpleSqlBuilder.getIdFieldName() + saveSql, ids[saveSql]);
					}

					Map<String, Object> entityParms = this.simpleSqlBuilder.getAllSqlParameter(entity);
					Iterator<Entry<String, Object>> arg8 = entityParms.entrySet().iterator();

					while (arg8.hasNext()) {
						Entry<String, Object> entry = (Entry<String, Object>) arg8.next();
						paramMap.put((String) entry.getKey() + saveSql, entry.getValue());
					}
				}

				String arg10 = this.simpleSqlBuilder.getBatchInsertSql(entitySize, hasIdTag);
				return this.namedParameterJdbcTemplate.update(arg10, paramMap);
			}
		}
	}

	public int saveOrUpdate(E entity) {
		int rows1;
		if (entity.isTransient()) {
			rows1 = this.save(entity);
		} else {
			rows1 = this.update(entity);
		}

		return rows1;
	}

	public List<Map<String, Object>> search(String sql, PagingParameter paging, Map<String, Object> params) {
		sql = this.handleSimpleSql(sql,params);
		return this.namedParameterJdbcTemplate.queryForList(this.pagingSqlBuilder.getPagingSql(sql, paging), params);
	}

	public List<Map<String, Object>> search(String sql, Map<String, Object> params) {
		return this.search(sql, (PagingParameter) null, params);
	}

	public int update(E entity) {
		if (entity.isTransient()) {
			throw new DaoAccessException("The record " + entity + " is transient!");
		} else {
			Map<String, Object> params = this.simpleSqlBuilder.getSqlParameters(entity);
			return this.namedParameterJdbcTemplate.update(this.simpleSqlBuilder.getUpdateSql(params), params);
		}
	}

	public int update(String sql, Map<String, Object> params) {
		if (!sql.toUpperCase().startsWith("UPDATE")) {
			sql = "UPDATE " + this.simpleSqlBuilder.getShardTable(params) + " SET " + sql;
		}

		return this.namedParameterJdbcTemplate.update(sql, params);
	}

	public List<Map<String, Object>> join(Condition condition, JoinItem... classLink) {
		return this.join(condition, "", classLink);
	}

	public List<Map<String, Object>> join(Condition condition, String orders, JoinItem... classLink) {
		Assert.state(classLink.length > 0, "单表请勿使用join功能");
		return this.join(condition, orders, this.fetchDefaultMapper(classLink), classLink);
	}

	private RowMapper<Map<String, Object>> fetchDefaultMapper(JoinItem... classLink) {
		return (rs, rowNum) -> {
			Map<String, Object> row = new HashMap<>();
			JoinItem[] arg3 = classLink;
			int arg4 = classLink.length;

			for (int arg5 = 0; arg5 < arg4; ++arg5) {
				JoinItem joinItem = arg3[arg5];
				if (joinItem.isHasItem()) {
					SimpleSqlBuilder<?> sqlBuilder = SimpleSqlBuilder.fetchSimpleSqlBuilder(joinItem.getClazz());
					Map<String, String> fieldColumnMapping = sqlBuilder.getFieldColumnMapping();
					String tableName = sqlBuilder.getTableName();
					List<String> keyList = joinItem.getEntityItems();
					if (CollectionUtils.isEmpty(joinItem.getEntityItems())) {
						keyList = new ArrayList<>(fieldColumnMapping.values());
					}

					(keyList).forEach((field) -> {
						try {
							row.put(tableName + "." + field, rs.getObject(tableName + "_" + field));
						} catch (SQLException arg6) {
							throw new DaoAccessException("设置字段时发生错误", arg6);
						}
					});
				}
			}

			return row;
		};
	}
	

	public <R> List<R> join(Condition condition, RowMapper<R> mapRowMapper, JoinItem... classLink) {
		return this.join(condition, (String) null, (RowMapper<R>) mapRowMapper, classLink);
	}
	
	public <R> List<R> join(Condition condition, String orders, RowMapper<R> rowMapper, JoinItem... classLink) {
		String sql = this.buildJoinSql(condition, orders, classLink);
		Map<String, Object> parms = this.fetchParms(condition);
		return this.namedParameterJdbcTemplate.query(sql, parms, rowMapper);
	}

	private Map<String, Object> fetchParms(Condition condition) {
		Map<String, Object> parms = Collections.emptyMap();
		if (condition != null) {
			parms = condition.getParameters();
		}

		return parms;
	}
	
	/**
	 * join 不支持分表操作，请勿进行分表连接
	 */
	public <R> R join(Condition condition, ResultSetExtractor<R> setExtractor, JoinItem... classLink) {
		return this.join(condition, (String) null, (ResultSetExtractor<R>) setExtractor, classLink);
	}
	
	/**
	 * join 不支持分表操作，请勿进行分表连接
	 */
	public <R> R join(Condition condition, String orders, ResultSetExtractor<R> setExtractor, JoinItem... classLink) {
		String sql = this.buildJoinSql(condition, orders, classLink);
		Map<String, Object> parms = this.fetchParms(condition);
		return this.namedParameterJdbcTemplate.query(sql, parms, setExtractor);
	}

	protected final String buildJoinSql(Condition condition, String orders, JoinItem... jiList) {
		Assert.state(jiList.length > 1, "单表请勿使用join功能");
		StringBuilder selectItemStr = new StringBuilder();
		StringBuilder joinTableStr = new StringBuilder();

		for (int sqlStr = 0; sqlStr < jiList.length; ++sqlStr) {
			JoinItem sql = jiList[sqlStr];
			Class<? extends Entity> clazz = sql.getClazz();
			SimpleSqlBuilder<?> sqlBuilder1 = SimpleSqlBuilder.fetchSimpleSqlBuilder(clazz);
			Map<String, String> fieldColumnMapping1 = sqlBuilder1.getFieldColumnMapping();
			String tableName1 = sqlBuilder1.getTableName();
			if (sqlStr == 0) {
//				tableName1 = this.simpleSqlBuilder.getSqlTalbeName();
				tableName1 = this.simpleSqlBuilder.getShardTable(condition.getParameters());
				joinTableStr.append(tableName1);
			}
			if (sql.isHasItem()) {
				selectItemStr.append(this.fetchSelectedField(sql.getEntityItems(), sqlBuilder1, fieldColumnMapping1,tableName1));
			} else {
				selectItemStr.append("");
			}
			
			if (sqlStr == jiList.length - 1) {
				break;
			}

			JoinItem rJoinItem = jiList[sqlStr + 1];
			Class<? extends Entity> rClass = rJoinItem.getClazz();
			SimpleSqlBuilder<?> rSqlBuilder = SimpleSqlBuilder.fetchSimpleSqlBuilder(rClass);
			String rTableName = rSqlBuilder.getTableName();
			

			joinTableStr.append(sql.getJoinType().fetchType()).append(rTableName).append(" ON ").append(tableName1)
					.append(".").append((String) fieldColumnMapping1.get(sqlBuilder1.getReferenceField(rClass)))
					.append(" = ").append(rTableName).append(".").append(sqlBuilder1.getReferencedColumn(rClass));
		}

		if (StringUtils.isEmpty(selectItemStr)) {
			throw new DaoAccessException("select的字段信息不能为空，目前没有指定要进行输出的字段信息");
		} else {
			selectItemStr.delete(selectItemStr.length() - 2, selectItemStr.length());
			StringBuilder arg15 = new StringBuilder(64);
			arg15.append("SELECT ").append(selectItemStr).append(" FROM ").append(joinTableStr);
			if (condition != null) {
				arg15.append(" WHERE ").append(condition.toSqlString());
			}

			if (StringUtils.hasLength(orders)) {
				arg15.append(" ORDER BY ").append(orders);
			}

			String arg16 = arg15.toString();
			log.debug(arg16);
			return arg16;
		}
	}

	private StringBuilder fetchSelectedField(List<String> items, SimpleSqlBuilder<? extends Entity> sqlBuilder,
			Map<String, String> fieldColumnMapping,String tableName) {
		HashSet<String> keySet = null;
		if (CollectionUtils.isEmpty(items)) {
			keySet = new HashSet<>(sqlBuilder.getFieldColumnMapping().values());
		} else {
			HashSet<String> fieldStr = new HashSet<>(items.size());
			items.forEach((item) -> {
				fieldStr.add(item);
			});
			keySet = fieldStr;
		}

		StringBuilder fieldStr1 = new StringBuilder();
//		String tableName = sqlBuilder.getTableName();
		Iterator<String> arg6 = keySet.iterator();

		while (arg6.hasNext()) {
			String field = (String) arg6.next();
			fieldStr1.append(tableName).append(".").append(field).append(" AS ").append(tableName).append("_")
					.append(field).append(", ");
		}

		return fieldStr1;
	}

	public DataStore<Map<String, Object>> join(Condition condition, PagingParameter paging, JoinItem... classLink) {
		return this.join(condition, "", paging, classLink);
	}

	public DataStore<Map<String, Object>> join(Condition condition, String orders, PagingParameter paging,
			JoinItem... classLink) {
		return this.join(condition, orders, this.fetchDefaultMapper(classLink), paging, classLink);
	}

	public <R> DataStore<R> join(Condition condition, RowMapper<R> rowMapper, PagingParameter paging,
			JoinItem... classLink) {
		return this.join(condition, "", rowMapper, paging, classLink);
	}

	public <R> DataStore<R> join(Condition condition, String orders, RowMapper<R> rowMapper, PagingParameter paging,
			JoinItem... jiList) {
		return this.joinTemplate(condition, orders, (sql, parms) -> {
			return this.namedParameterJdbcTemplate.query(sql, parms, rowMapper);
		}, paging, jiList);
	}

	private <R> DataStore<R> joinTemplate(Condition condition, String orders, DataBuilder<R> dataBuilder,
			PagingParameter paging, JoinItem... jiList) {
		String sql = this.buildJoinSql(condition, orders, jiList);
		Map<String, Object> parms = this.fetchParms(condition);
		int records = ((Integer) this.namedParameterJdbcTemplate.queryForObject(this.pagingSqlBuilder.getCountSql(sql),
				parms, Integer.class)).intValue();
		List<R> datas = Collections.emptyList();
		if (records > 0) {
			datas = dataBuilder.builder(sql, parms);
		}

		return new DataStore<R>(records, datas);
	}

	public <R> DataStore<R> join(Condition condition, ResultSetExtractor<List<R>> rowMapper, PagingParameter paging,
			JoinItem... classLink) {
		return this.join(condition, "", rowMapper, paging, classLink);
	}

	public <R> DataStore<R> join(Condition condition, String orders, ResultSetExtractor<List<R>> setExtractor,
			PagingParameter paging, JoinItem... jiList) {
		return this.joinTemplate(condition, orders, (sql, parms) -> {
			return (List<R>) this.namedParameterJdbcTemplate.query(sql, parms, setExtractor);
		}, paging, jiList);
	}
	
	@FunctionalInterface
	private interface DataBuilder<R>{
		List<R> builder(String var1, Map<String,Object> var2);
	}
}