package com.igg.boot.framework.exception;

import org.springframework.http.HttpStatus;

public class BindException extends FrameworkSystemException {
	private BindExceptionCode exCode;

	public BindException(BindExceptionCode code) {
		this(code, null, "");
	}

	public BindException(BindExceptionCode code, Throwable cause) {
		this(code, cause, "");
	}

	public BindException(BindExceptionCode code, String appendMsg) {
		this(code, null, appendMsg);
	}

	public BindException(BindExceptionCode code, Throwable cause, String appendMsg) {
		super(code.getMessage() + appendMsg, cause);
		exCode = code;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2362361704856495519L;

	@Override
	public int getCode() {
		return exCode.getCode();
	}

	@Override
	public HttpStatus getHttpStatus() {
		return exCode.getHttpStatus();
	}

	@Override
	public FrameworkSystemExceptionCode getExceptionCode() {
		return this.exCode;
	}

}
