package com.igg.boot.framework.jdbc.persistence.exception;

import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;

public class DuplicateRecordException extends HttpSystemException {
	private static final long serialVersionUID = 9221438805698085718L;

	public DuplicateRecordException(String appendMsg) {
		super(HttpSystemExceptionCode.DB_ANNOTATION_NOT_FOUND_ERROR, appendMsg);
	}
}