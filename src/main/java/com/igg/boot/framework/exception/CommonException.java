package com.igg.boot.framework.exception;

import org.springframework.http.HttpStatus;

public class CommonException extends FrameworkSystemException{
    private static final long serialVersionUID = 6502491918007587465L;
    private CommonExceptionCode exCode;

    public CommonException(CommonExceptionCode code) {
        this(code, null, "");
    }

    public CommonException(CommonExceptionCode code, Throwable cause) {
        this(code, cause, "");
    }

    public CommonException(CommonExceptionCode code, String appendMsg) {
        this(code, null, appendMsg);
    }

    public CommonException(CommonExceptionCode code, Throwable cause, String appendMsg) {
        super(code.getMessage() + appendMsg, cause);
        exCode = code;
    }

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
        return exCode;
    }

}
