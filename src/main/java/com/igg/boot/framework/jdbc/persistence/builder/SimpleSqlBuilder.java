package com.igg.boot.framework.jdbc.persistence.builder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.igg.boot.framework.autoconfigure.ApplicationContextHolder;
import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;
import com.igg.boot.framework.jdbc.persistence.Entity;
import com.igg.boot.framework.jdbc.persistence.annotation.Column;
import com.igg.boot.framework.jdbc.persistence.annotation.Id;
import com.igg.boot.framework.jdbc.persistence.annotation.Reference;
import com.igg.boot.framework.jdbc.persistence.annotation.Table;
import com.igg.boot.framework.jdbc.persistence.condition.Condition;
import com.igg.boot.framework.jdbc.persistence.exception.DBAnnotationException;
import com.igg.boot.framework.jdbc.persistence.exception.DaoAccessException;
import com.igg.boot.framework.jdbc.persistence.shard.ShardStrategy;
import com.igg.boot.framework.jdbc.persistence.shard.ShardStrategyMap;
import com.igg.boot.framework.jdbc.persistence.utils.ObjectUtil;

public class SimpleSqlBuilder<E extends Entity> {
	private static final Logger log = LoggerFactory.getLogger(SimpleSqlBuilder.class);
	private static final Map<Class<? extends Entity>, SimpleSqlBuilder<? extends Entity>> BUILDER = new ConcurrentHashMap<>(
			8);
	private final String tableName;
	private final String sqlTalbeName;
	private final String idFieldName;
	private final Field idField;
	private final Map<String, String> fieldColumnMapping;
	private final Class<E> entityClass;
	
	@SuppressWarnings("unchecked")
	public static <E extends Entity> SimpleSqlBuilder<E> fetchSimpleSqlBuilder(Class<E> clazz) {
		SimpleSqlBuilder<E> builder = (SimpleSqlBuilder<E>) BUILDER.get(clazz);
		if (null == builder) {
			BUILDER.putIfAbsent(clazz, new SimpleSqlBuilder<>(clazz));
		}

		return (SimpleSqlBuilder<E>) BUILDER.get(clazz);
	}
	
	private SimpleSqlBuilder(Class<E> entityClass) {
		this.entityClass = entityClass;
		List<Field> columnFields = ObjectUtil.getFieldsByAnnotation(entityClass, Column.class);
		if (CollectionUtils.isEmpty(columnFields)) {
			throw new DBAnnotationException(
					"Annotation " + Column.class.getName() + " not found for " + entityClass.getName());
		} else {
			this.idField = this.fetchIdField(columnFields);
			this.idFieldName = this.idField.getName();
			this.fieldColumnMapping = this.fetchFieldColumnMapping(columnFields);
		}
		
		this.tableName = this.fetchTableName(entityClass);
		this.sqlTalbeName = "`" + this.tableName + "`";
	}

	private String fetchTableName(Class<E> entityClass) {
		Table annotation = (Table) entityClass.getAnnotation(Table.class);
		String tableName = "";
		if (annotation == null) {
			tableName = entityClass.getSimpleName();
		} else {
			tableName = annotation.value();
		}

		return tableName;
	}

	private Field fetchIdField(List<Field> columnFields) {
		Field idField = null;
		Iterator<Field> arg2 = columnFields.iterator();

		while (arg2.hasNext()) {
			Field field = (Field) arg2.next();
			Id id = (Id) field.getAnnotation(Id.class);
			if (id != null) {
				if (idField != null) {
					throw new HttpSystemException(HttpSystemExceptionCode.DB_FIELD_COLUMN_MAPPING_ERROR,
							"Id field not unique for " + this.entityClass.getName());
				}

				idField = field;
				if (!Number.class.isAssignableFrom(field.getType())) {
					throw new DBAnnotationException(field.getName() + " must be assignable from Number");
				}
			}
		}

		if (idField == null) {
			throw new DBAnnotationException(
					"Annotation " + Id.class.getName() + " not found for " + this.entityClass.getName());
		} else {
			return idField;
		}
	}

	private Map<String, String> fetchFieldColumnMapping(List<Field> columnFields) {
		Map<String, String> mapping = new HashMap<>();

		Field field;
		String columnName;
		for (Iterator<Field> arg2 = columnFields.iterator(); arg2.hasNext(); mapping.put(field.getName(), columnName)) {
			field = (Field) arg2.next();
			columnName = "";
			if (field.isAnnotationPresent(Column.class)) {
				columnName = ((Column) field.getAnnotation(Column.class)).value();
				if (StringUtils.isEmpty(columnName)) {
					columnName = field.getName();
				}
			} else {
				columnName = field.getName();
			}
		}

		return Collections.unmodifiableMap(mapping);
	}

	public Optional<String> getColumnField(String column) {
		Iterator<Entry<String, String>> arg1 = this.fieldColumnMapping.entrySet().iterator();

		Entry<String, String> entry;
		do {
			if (!arg1.hasNext()) {
				return Optional.empty();
			}

			entry = (Entry<String, String>) arg1.next();
		} while (!((String) entry.getValue()).equalsIgnoreCase(column));

		return Optional.of(entry.getKey().toString());
	}

	public String getQuerySimpleSql(Number id) {
		StringBuilder sqlBuf = new StringBuilder(128);
		Map<String,Object> params = new HashMap<>(1);
		params.put(this.idFieldName, id);
		String tableName = getShardTable(params);
		sqlBuf.append("SELECT * FROM ").append(tableName).append(" WHERE ").append("`")
				.append((String) this.fieldColumnMapping.get(this.idFieldName)).append("`").append(" =:")
				.append(this.getIdFieldName());
		String sql = sqlBuf.toString();
		log.debug(sql);
		return sql;
	}

	public String getQueryAllSql() {
		String sql = "SELECT * FROM " + this.sqlTalbeName;
		log.debug(sql);
		return sql;
	}
	
	public String getQueryAllSql(Map<String,Object> params) {
		String tableName = getShardTable(params);
		String sql = "SELECT * FROM " + tableName;
		log.debug(sql);
		return sql;
	}

	public String getQueryCountSql() {
		String sql = "SELECT count(*) FROM " + this.sqlTalbeName;
		log.debug(sql);
		return sql;
	}
	
	public String getQueryCountSql(Map<String,Object> params) {
		String sql = "SELECT count(*) FROM " + getShardTable(params);
		log.debug(sql);
		return sql;
	}
	
	public String getShardTable(Map<String,Object> params) {
		String tableName = this.tableName;
		ShardStrategyMap shardStrategyMap = (ShardStrategyMap) ApplicationContextHolder.getBean(ShardStrategyMap.class);
		ShardStrategy<? extends Entity> shardStrategy = shardStrategyMap.getShardStrategyMap().get(this.entityClass);
		if(shardStrategy != null) {
			tableName = this.tableName + shardStrategy.getShardName(params);
		}
		
		return tableName;
	}

	public String getDeleteSql(Number id) {
		StringBuilder sqlStr = new StringBuilder(128);
		Map<String,Object> params = new HashMap<>(1);
		params.put(this.idFieldName, id);
		String tableName = getShardTable(params);
		sqlStr.append("DELETE FROM ").append(tableName).append(" WHERE ").append("`")
				.append((String) this.fieldColumnMapping.get(this.idFieldName)).append("`").append(" =:")
				.append(this.getIdFieldName());
		String sql = sqlStr.toString();
		log.debug(sql);
		return sql;
	}

	public String getDeleteInSql() {
		StringBuilder sqlStr = new StringBuilder(128);
		sqlStr.append("DELETE FROM ").append(this.sqlTalbeName).append(" WHERE ").append("`")
				.append((String) this.fieldColumnMapping.get(this.idFieldName)).append("`").append(" IN (:")
				.append(this.getIdFieldName()).append(")");
		String sql = sqlStr.toString();
		log.debug(sql);
		return sql;
	}

	public String getDeleteContidionSql(Condition condition) {
		String where = condition.toSqlString();
		Assert.state(StringUtils.hasText(where), "使用的条件禁止为空");
		StringBuilder sqlStr = new StringBuilder(128);
		String tableName = getShardTable(condition.getParameters());
		
		sqlStr.append("DELETE FROM ").append(tableName).append(" WHERE ").append(where);
		return sqlStr.toString();
	}
	
	public String getInsertSql(Map<String,Object> params) {
		String sql = this.getInsertSql(params.keySet(), false, null);
		String tableName = getShardTable(params);
		
		return "INSERT INTO " + tableName + sql;
	}

	private String getInsertSql(Set<String> fieldSet, boolean isIncludeIdField, Number id) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		//sb.append("INSERT INTO ").append(this.sqlTalbeName).append("(");
		sb.append("(");
		Iterator<String> sql = fieldSet.iterator();

		while (true) {
			String field;
			do {
				if (!sql.hasNext()) {
					sb.delete(sb.length() - 2, sb.length());
					sb2.delete(sb2.length() - 2, sb2.length());
					sb.append(") VALUES(");
					sb.append(sb2);
					sb.append(")");
					String sql1 = sb.toString();
					log.debug(sql1);
					return sql1;
				}

				field = (String) sql.next();
			} while (field.equals(this.idFieldName) && !isIncludeIdField);

			sb.append("`").append((String) this.fieldColumnMapping.get(field)).append("`").append(", ");
			sb2.append(":").append(field).append(", ");
		}
	}

	public String getBatchInsertSql(int size, boolean isIncludeIdField) {
		StringBuilder sb = new StringBuilder(64);
		StringBuilder[] condition = new StringBuilder[size];

		int sql;
		for (sql = 0; sql < size; ++sql) {
			condition[sql] = new StringBuilder(64);
		}

		sb.append("INSERT INTO ").append(this.sqlTalbeName).append("(");
		Iterator<Entry<String, String>> arg7 = this.fieldColumnMapping.entrySet().iterator();

		while (true) {
			Entry<String, String> entry;
			do {
				if (!arg7.hasNext()) {
					sb.delete(sb.length() - 2, sb.length());

					for (sql = 0; sql < size; ++sql) {
						condition[sql].delete(condition[sql].length() - 2, condition[sql].length());
					}

					sb.append(") VALUES ");

					for (sql = 0; sql < size; ++sql) {
						sb.append("(").append(condition[sql]).append("), ");
					}

					sb.delete(sb.length() - 2, sb.length());
					String arg8 = sb.toString();
					log.debug(arg8);
					return arg8;
				}

				entry = (Entry<String, String>) arg7.next();
			} while (((String) entry.getKey()).equals(this.idFieldName) && !isIncludeIdField);

			sb.append("`").append((String) entry.getValue()).append("`").append(", ");

			for (int i = 0; i < size; ++i) {
				condition[i].append(":").append((String) entry.getKey()).append(i).append(", ");
			}
		}
	}
	
	public String getIncludeIdFieldInsertSql(Map<String,Object> params, Number id) {
		String sql = this.getInsertSql(params.keySet(), true, id);
		String tableName = getShardTable(params);
		return "INSERT INTO " + tableName + sql;
	}

	public String getUpdateSql(Map<String,Object> params) {
		StringBuilder sb = new StringBuilder();
		String tableName = getShardTable(params);
		sb.append("UPDATE ").append(tableName).append(" SET");
		Iterator<String> sql = params.keySet().iterator();

		while (sql.hasNext()) {
			String field = (String) sql.next();
			if (!field.equals(this.idFieldName)) {
				sb.append(" ").append("`").append((String) this.fieldColumnMapping.get(field)).append("`")
						.append(" =:" + field + ",");
			}
		}

		sb.deleteCharAt(sb.length() - 1);
		sb.append(" WHERE ").append((String) this.fieldColumnMapping.get(this.idFieldName)).append(" =:")
				.append(this.idFieldName);
		String sql1 = sb.toString();
		log.debug(sql1);
		return sql1;
	}

	public Map<String, Object> getSqlParameters(E entity) {
		return this.getSqlParameters(entity, true);
	}

	public Map<String, Object> getAllSqlParameter(E entity) {
		return this.getSqlParameters(entity, false);
	}

	private Map<String, Object> getSqlParameters(E entity, boolean isFilter) {
		Map<String, Object> params = new HashMap<>();
		Iterator<String> arg3 = this.fieldColumnMapping.keySet().iterator();

		while (arg3.hasNext()) {
			String field = (String) arg3.next();

			try {
				Field e = this.entityClass.getDeclaredField(field);
				e.setAccessible(true);
				Object value = e.get(entity);
				//Object value = (new PropertyDescriptor(field, this.entityClass)).getReadMethod().invoke(entity,
				//		new Object[0]);
				if (value == null) {
					if (!field.equals(this.idFieldName)) {
						if (isFilter) {
							Column column = (Column) e.getAnnotation(Column.class);
							if (column.isNotNull()) {
								if (!column.hasDefault()) {
									throw new DaoAccessException("Column field " + field + " value error for "
											+ this.entityClass.getName() + ", the value can\'t is null!");
								}
								continue;
							}
						}

						params.put(field, (Object) null);
					}
				} else if (e.getType().isEnum()) {
					params.put(field,
							e.getType().getDeclaredMethod("getCode", new Class[0]).invoke(value, new Object[0]));
				} else {
					params.put(field, value);
				}
			} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					 | NoSuchMethodException | NoSuchFieldException arg8) {
				log.error("获取参数信息时发生异常");
				throw new DaoAccessException("获取参数信息时发生异常", arg8);
			}
		}

		return params;
	}

	public String getReferenceField(Class<? extends Entity> referencedClass) {
		Iterator<Field> arg1 = ObjectUtil.getFieldsByAnnotation(this.entityClass, Reference.class).iterator();

		Field field;
		Class<?> referenceValue;
		do {
			if (!arg1.hasNext()) {
				throw new DaoAccessException("无法找到匹配的外键信息");
			}

			field = (Field) arg1.next();
			Reference reference = (Reference) field.getAnnotation(Reference.class);
			referenceValue = reference.value();
			if (referenceValue == Entity.REFERENCE_CLASS_DEFAULT) {
				referenceValue = this.entityClass;
			}
		} while (referenceValue != referencedClass);

		return field.getName();
	}

	public String getReferencedColumn(Class<? extends Entity> referencedClass) {
		Iterator<Field> arg1 = ObjectUtil.getFieldsByAnnotation(this.entityClass, Reference.class).iterator();

		Field field;
		Reference reference;
		Class<?> referenceValue;
		do {
			if (!arg1.hasNext()) {
				throw new DaoAccessException("无法获得指定被引用类型的被引用列名");
			}

			field = (Field) arg1.next();
			reference = (Reference) field.getAnnotation(Reference.class);
			referenceValue = reference.value();
			if (referenceValue == Entity.REFERENCE_CLASS_DEFAULT) {
				referenceValue = this.entityClass;
			}
		} while (referenceValue != referencedClass);

		String column = reference.column();
		if (column.equals("")) {
			Field columStr = (Field) ObjectUtil.getFieldsByAnnotation(referencedClass, Id.class).get(0);
			column = ((Column) columStr.getAnnotation(Column.class)).value();
			if (column.equals("")) {
				column = columStr.getName().toUpperCase();
			}
		}
		Optional<String> cl = new SimpleSqlBuilder<>(referencedClass).getColumnField(column);
		String tmpColumn = column;
		String columnField = cl.orElseThrow(() -> {
			return new DaoAccessException("Column name " + tmpColumn + " not exist for " + referencedClass.getName());
		});

		Class<?> columnFieldType = null;

		try {
			columnFieldType = referencedClass.getDeclaredField(columnField).getType();
		} catch (SecurityException | NoSuchFieldException arg10) {
			throw new DaoAccessException("获取字段信息失败:" + columnField, arg10);
		}

		if (columnFieldType != field.getType()) {
			StringBuilder msg = new StringBuilder(64);
			msg.append("Reference ").append(this.entityClass.getName()).append(" field ").append(field.getName())
					.append(" type ").append(field.getType().getName()).append(" and referenced ")
					.append(referencedClass.getName()).append(" field ").append(columnField).append(" type ")
					.append(columnFieldType.getName()).append(" not mapping, type must same!");
			throw new DaoAccessException(msg.toString());
		} else {
			return column;
		}
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getSqlTalbeName() {
		return this.sqlTalbeName;
	}

	public String getIdFieldName() {
		return this.idFieldName;
	}

	public Field getIdField() {
		return this.idField;
	}

	public Map<String, String> getFieldColumnMapping() {
		return this.fieldColumnMapping;
	}

	public Class<E> getEntityClass() {
		return this.entityClass;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof SimpleSqlBuilder)) {
			return false;
		} else {
			SimpleSqlBuilder<?> other = (SimpleSqlBuilder<?>) o;
			if (!other.canEqual(this)) {
				return false;
			} else {
				String this$tableName = this.getTableName();
				String other$tableName = other.getTableName();
				if (this$tableName == null) {
					if (other$tableName != null) {
						return false;
					}
				} else if (!this$tableName.equals(other$tableName)) {
					return false;
				}

				String this$sqlTalbeName = this.getSqlTalbeName();
				String other$sqlTalbeName = other.getSqlTalbeName();
				if (this$sqlTalbeName == null) {
					if (other$sqlTalbeName != null) {
						return false;
					}
				} else if (!this$sqlTalbeName.equals(other$sqlTalbeName)) {
					return false;
				}

				String this$idFieldName = this.getIdFieldName();
				String other$idFieldName = other.getIdFieldName();
				if (this$idFieldName == null) {
					if (other$idFieldName != null) {
						return false;
					}
				} else if (!this$idFieldName.equals(other$idFieldName)) {
					return false;
				}

				label62: {
					Field this$idField = this.getIdField();
					Field other$idField = other.getIdField();
					if (this$idField == null) {
						if (other$idField == null) {
							break label62;
						}
					} else if (this$idField.equals(other$idField)) {
						break label62;
					}

					return false;
				}

				label55: {
					Map<String, String> this$fieldColumnMapping = this.getFieldColumnMapping();
					Map<String, String> other$fieldColumnMapping = other.getFieldColumnMapping();
					if (this$fieldColumnMapping == null) {
						if (other$fieldColumnMapping == null) {
							break label55;
						}
					} else if (this$fieldColumnMapping.equals(other$fieldColumnMapping)) {
						break label55;
					}

					return false;
				}

				Class<?> this$entityClass = this.getEntityClass();
				Class<?> other$entityClass = other.getEntityClass();
				if (this$entityClass == null) {
					if (other$entityClass != null) {
						return false;
					}
				} else if (!this$entityClass.equals(other$entityClass)) {
					return false;
				}

				return true;
			}
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof SimpleSqlBuilder;
	}

	public int hashCode() {
		byte result = 1;
		String $tableName = this.getTableName();
		int result1 = result * 59 + ($tableName == null ? 43 : $tableName.hashCode());
		String $sqlTalbeName = this.getSqlTalbeName();
		result1 = result1 * 59 + ($sqlTalbeName == null ? 43 : $sqlTalbeName.hashCode());
		String $idFieldName = this.getIdFieldName();
		result1 = result1 * 59 + ($idFieldName == null ? 43 : $idFieldName.hashCode());
		Field $idField = this.getIdField();
		result1 = result1 * 59 + ($idField == null ? 43 : $idField.hashCode());
		Map<String, String> $fieldColumnMapping = this.getFieldColumnMapping();
		result1 = result1 * 59 + ($fieldColumnMapping == null ? 43 : $fieldColumnMapping.hashCode());
		Class<E> $entityClass = this.getEntityClass();
		result1 = result1 * 59 + ($entityClass == null ? 43 : $entityClass.hashCode());
		return result1;
	}

	public String toString() {
		return "SimpleSqlBuilder(tableName=" + this.getTableName() + ", sqlTalbeName=" + this.getSqlTalbeName()
				+ ", idFieldName=" + this.getIdFieldName() + ", idField=" + this.getIdField() + ", fieldColumnMapping="
				+ this.getFieldColumnMapping() + ", entityClass=" + this.getEntityClass() + ")";
	}
}