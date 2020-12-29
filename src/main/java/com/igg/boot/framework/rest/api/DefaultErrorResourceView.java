package com.igg.boot.framework.rest.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.View;

import com.igg.boot.framework.exception.HttpSystemExceptionCode;

public class DefaultErrorResourceView implements View {
	public static final String TEMPLATE_STR_CODE = "code";
	public static final String TEMPLATE_STR_MESSAGE = "message";
	private static final String TEMPLATE_STR_CODE_TPL = new StringBuilder(16).append("${").append(TEMPLATE_STR_CODE)
			.append("}").toString();
	private static final String TEMPLATE_STR_MESSAGE_TPL = new StringBuilder(16).append("${")
			.append(TEMPLATE_STR_MESSAGE).append("}").toString();

	private static final String TEMPLATE_STR = "<!DOCTYPE html><html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"
			+ "<title>error message</title><style>body{margin:0;margin:auto;"
			+ "width:50pc;color:#666;text-align:center;font-size:.875rem;font-family:Helvetica Neue,Helvetica,Arial,sans-serif}"
			+ "h1{color:#456;font-weight:400;font-size:3.5rem;line-height:75pt}h2{color:#666;font-size:1.5rem;line-height:1.5em}"
			+ "h3{color:#456;font-weight:400;font-size:1.25rem;line-height:1.75rem}"
			+ "hr{margin:18px 0;border:0;border-top:1px solid #eee;border-bottom:1px solid #fff}</style></head><body><h1>"
			+ TEMPLATE_STR_CODE_TPL + "</h1><h3>" + TEMPLATE_STR_MESSAGE_TPL + "</h3></body></html>";

	@Override
	public String getContentType() {
		return MediaType.TEXT_HTML_VALUE;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType(getContentType());

		String code = model.containsKey(TEMPLATE_STR_CODE) ? model.get(TEMPLATE_STR_CODE).toString()
				: HttpSystemExceptionCode.SYSTEM_ERROR.getCode() + "";
		String message = model.containsKey(TEMPLATE_STR_CODE) ? model.get(TEMPLATE_STR_MESSAGE).toString()
				: getCommonErrorMsg(model);
		String content = TEMPLATE_STR.replace(TEMPLATE_STR_CODE_TPL, code).replace(TEMPLATE_STR_MESSAGE_TPL, message);

		FileCopyUtils.copy(content.getBytes(), response.getOutputStream());
	}

	private String getCommonErrorMsg(Map<String, ?> model) {
		return HttpSystemExceptionCode.SYSTEM_ERROR.getMessage() + ":status = " + model.get("status") + ",error = "
				+ model.get("error") + ", message = " + model.get("message") + ",request_id = " + model.get("request_id");
	}

}