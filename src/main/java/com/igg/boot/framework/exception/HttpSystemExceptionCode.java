package com.igg.boot.framework.exception;

import org.springframework.http.HttpStatus;

public class HttpSystemExceptionCode extends CommonExceptionCode {
	public static final HttpSystemExceptionCode SIGN_ERROR = new HttpSystemExceptionCode(10002, "签名校验失败");
	public static final HttpSystemExceptionCode TIMESTAMP_ERROR = new HttpSystemExceptionCode(10003, "时间戳过期");
	public static final HttpSystemExceptionCode ENCRYPT_NOT_EXIST = new HttpSystemExceptionCode(10004, "加密算法不存在");
	public static final HttpSystemExceptionCode APP_NOT_EXIST = new HttpSystemExceptionCode(10005, "应用不存在");
	public static final HttpSystemExceptionCode DB_DAO_RECORDE_ERROR = new HttpSystemExceptionCode(10006, "数据重复",
			HttpStatus.INTERNAL_SERVER_ERROR);
	public static final HttpSystemExceptionCode DB_ANNOTATION_NOT_FOUND_ERROR = new HttpSystemExceptionCode(10007,
			"数据库注解不存在", HttpStatus.INTERNAL_SERVER_ERROR);
	public static final HttpSystemExceptionCode DB_PAGING_SQL_BUILDER_ERROR = new HttpSystemExceptionCode(10008,
			"分页数据有误", HttpStatus.INTERNAL_SERVER_ERROR);
	public static final HttpSystemExceptionCode DB_FIELD_COLUMN_MAPPING_ERROR = new HttpSystemExceptionCode(10009,
			"字段映射有误", HttpStatus.INTERNAL_SERVER_ERROR);
	public static final HttpSystemExceptionCode METHOD_PARAM_ERROR = new HttpSystemExceptionCode(10010, "参数有误",
			HttpStatus.INTERNAL_SERVER_ERROR);
	public static final HttpSystemExceptionCode ES_RANGE_CONDITION = new HttpSystemExceptionCode(10011,"范围查询没有指定范围");
	public static final HttpSystemExceptionCode ES_ANNOTATION_NOT_FOUND_ERROR = new HttpSystemExceptionCode(10012,"ES指定的注解不存在");
	public static final HttpSystemExceptionCode ES_ANNOTATION_ROUTING_NOT_FOUND_ERROR = new HttpSystemExceptionCode(10013,"ES指定的routing不存在");
	public static final HttpSystemExceptionCode PAGE_NOT_FOUND = new HttpSystemExceptionCode(10000,"404",HttpStatus.NOT_FOUND);
	
	public HttpSystemExceptionCode(int code, String message) {
		super(code, message);
	}

	public HttpSystemExceptionCode(int code, String message, HttpStatus httpStatus) {
		super(code, message, httpStatus);
	}
}
