package com.igg.boot.framework.exception;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public abstract class FrameworkSystemException extends NestedRuntimeException {
	public FrameworkSystemException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public FrameworkSystemException(String msg) {
		super(msg);
	}

	public abstract int getCode();
	
	public abstract HttpStatus getHttpStatus();
	
	public abstract FrameworkSystemExceptionCode getExceptionCode();
	
}
