package com.igg.boot.framework.jdbc.persistence.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;
import com.igg.boot.framework.jdbc.persistence.bean.PagingParameter;

public class PagingSqlBuilder {
	private static final Logger log = LoggerFactory.getLogger(PagingSqlBuilder.class);
//	private static final String ORACLE = "oracle";
//	private static final String MYSQL = "mysql";
//	private static final String SQLSERVER = "sqlserver";
	private String dataBaseType = "mysql";

	public PagingSqlBuilder() {
	}

	public PagingSqlBuilder(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}

	public String getDataBaseType() {
		return this.dataBaseType;
	}

	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}

	public String getCountSql(String rawSql) {
		String countSql = "SELECT COUNT(*) AS RECORDS " + rawSql.substring(rawSql.toUpperCase().indexOf("FROM"));
		int orderIndex = countSql.toUpperCase().lastIndexOf("ORDER");
		if (orderIndex >= 0) {
			countSql = countSql.substring(0, orderIndex).trim();
		}

		log.debug(countSql);
		return countSql;
	}

	public String getGroupCountSql(String rawSql) {
		String groupCountSql = "SELECT SUM(RECORDS) FROM (" + this.getCountSql(rawSql) + ") AS T";
		log.debug(groupCountSql);
		return groupCountSql;
	}

	public String getPagingSql(String rawSql, PagingParameter paging) {
		if (paging != null && !paging.isInvalid()) {
			int rows = paging.getLimit();
			int start = paging.getStart();
			int end = start + rows;
			String pagingSql;
			if (this.dataBaseType.toLowerCase().indexOf("oracle") >= 0) {
				pagingSql = "SELECT T.*, ROWNUM AS ROW_NUM FROM (" + rawSql + ") AS T WHERE ROWNUM < " + end;
				if (start == 0) {
					log.debug(pagingSql);
					return pagingSql;
				} else {
					pagingSql = "SELECT * FROM (" + pagingSql + ") AS T_O WHERE ROW_NUM >= " + start;
					log.debug(pagingSql);
					return pagingSql;
				}
			} else if (this.dataBaseType.toLowerCase().indexOf("mysql") >= 0) {
				pagingSql = rawSql + " LIMIT " + start + ", " + rows;
				log.debug(pagingSql);
				return pagingSql;
			} else if (this.dataBaseType.toLowerCase().indexOf("sqlserver") >= 0) {
				pagingSql = "SELECT TOP " + end + rawSql.trim().substring(6);
				if (start == 0) {
					log.debug(pagingSql);
					return pagingSql;
				} else {
					String orders = rawSql.substring(rawSql.toUpperCase().lastIndexOf("ORDER"));
					int subIndex = rawSql.toUpperCase().indexOf("FROM") - 1;
					pagingSql = "SELECT * FROM (" + rawSql.substring(0, subIndex) + ", ROW_NUMBER() OVER(" + orders
							+ ") AS ROW_NUM "
							+ rawSql.substring(subIndex, rawSql.toUpperCase().lastIndexOf("ORDER")).trim()
							+ ") AS T WHERE ROW_NUM >= " + start + " AND ROW_NUM < " + end;
					log.debug(pagingSql);
					return pagingSql;
				}
			} else {
				throw new HttpSystemException(HttpSystemExceptionCode.DB_PAGING_SQL_BUILDER_ERROR);
			}
		} else {
			log.debug(rawSql);
			return rawSql;
		}
	}
}