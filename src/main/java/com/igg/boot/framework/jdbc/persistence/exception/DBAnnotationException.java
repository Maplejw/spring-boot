package com.igg.boot.framework.jdbc.persistence.exception;

import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;

public class DBAnnotationException extends HttpSystemException {
	private static final long serialVersionUID = 8156743567301796875L;

	public DBAnnotationException(String appendMsg, Throwable cause) {
		super(HttpSystemExceptionCode.DB_ANNOTATION_NOT_FOUND_ERROR, cause, appendMsg);
	}

	public DBAnnotationException(String appendMsg) {
		super(HttpSystemExceptionCode.DB_ANNOTATION_NOT_FOUND_ERROR, appendMsg);
	}
}