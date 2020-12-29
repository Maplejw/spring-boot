package com.igg.boot.framework.rest.api;

import lombok.Data;

@Data
public class RestResponse<T> {
	private int code;
	private String message;
	private T data;
	private String requestId;
}
