package com.igg.boot.framework.autoconfigure.es.condition.exception;

import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;

public class ElastisearchException extends HttpSystemException {
	private static final long serialVersionUID = 3695531363803355998L;
	
	public ElastisearchException(HttpSystemExceptionCode code) {
        super(code,"");
    }


}
