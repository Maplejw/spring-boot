package com.igg.boot.framework.jdbc.persistence.exception;

import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;

public class DaoAccessException extends HttpSystemException {
	private static final long serialVersionUID = -1401926028683164942L;

	public DaoAccessException(String appendMsg, Throwable cause) {
		super(HttpSystemExceptionCode.DB_DAO_RECORDE_ERROR, cause, appendMsg);
	}

	public DaoAccessException(String appendMsg) {
		this(appendMsg, (Throwable) null);
	}
}