package com.igg.boot.framework.exception;

import org.springframework.http.HttpStatus;

public class BindExceptionCode extends FrameworkSystemExceptionCode {
	public static final BindExceptionCode ERROR_PARM = new BindExceptionCode(10001, "参数异常:");

	public BindExceptionCode(int code, String message) {
		super(code, message);
	}

	public BindExceptionCode(int code, String message, HttpStatus httpStatus) {
		super(code, message, httpStatus);
	}
}
