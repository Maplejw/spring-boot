package com.igg.boot.framework.exception;

import org.springframework.http.HttpStatus;

public class HttpSystemException extends CommonException {
	private HttpSystemExceptionCode exCode;

	public HttpSystemException(HttpSystemExceptionCode code) {
		this(code, null, "");
	}

	public HttpSystemException(HttpSystemExceptionCode code, Throwable cause) {
		this(code, cause, "");
	}

	public HttpSystemException(HttpSystemExceptionCode code, String appendMsg) {
		this(code, null, appendMsg);
	}

	public HttpSystemException(HttpSystemExceptionCode code, Throwable cause, String appendMsg) {
		super(code,cause,appendMsg);
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
