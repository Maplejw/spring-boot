package com.igg.boot.framework.exception;

import org.springframework.http.HttpStatus;

public class CommonExceptionCode extends FrameworkSystemExceptionCode{
    public static final CommonExceptionCode SYSTEM_ERROR = new CommonExceptionCode(10000, "未知的系统异常",
            HttpStatus.INTERNAL_SERVER_ERROR);
    
    public CommonExceptionCode(int code, String message) {
        super(code, message);
    }
    
    public CommonExceptionCode(int code, String message, HttpStatus httpStatus) {
        super(code, message, httpStatus);
    }

}
