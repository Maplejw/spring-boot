package com.igg.boot.framework.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public abstract class FrameworkSystemExceptionCode {
	private int code;
	private String message;
	private HttpStatus httpStatus;

	public FrameworkSystemExceptionCode(int code, String message) {
		this(code, message, HttpStatus.OK);
	}

	public FrameworkSystemExceptionCode(int code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}

}
