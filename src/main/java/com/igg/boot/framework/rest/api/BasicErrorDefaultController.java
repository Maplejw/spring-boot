package com.igg.boot.framework.rest.api;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.igg.boot.framework.exception.FrameworkSystemException;
import com.igg.boot.framework.exception.HttpSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;

@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorDefaultController extends AbstractErrorController {
	@Autowired
	private DefaultErrorAttributes defaultErrorAttributes;
	
	public BasicErrorDefaultController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	@Override
	public String getErrorPath() {
		return null;
	}

	@RequestMapping(produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, true));
		response.setStatus(status.value());

		ModelAndView modelAndView = resolveErrorView(request, response, status, model);

		return (modelAndView != null ? modelAndView : new ModelAndView("error", model));
	}

	@RequestMapping
	@RestResponseBody
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		WebRequest webRequest = new ServletWebRequest(request);
		Throwable cause = defaultErrorAttributes.getError(webRequest);
		if(cause != null && cause instanceof FrameworkSystemException) {
			FrameworkSystemException ex = (FrameworkSystemException) cause;
			throw new HttpSystemException((HttpSystemExceptionCode)ex.getExceptionCode());
		}else {
			Map<String, Object> body = getErrorAttributes(request, true);
			if(body.get("status").toString().equals("404")) {
			    throw new HttpSystemException(HttpSystemExceptionCode.PAGE_NOT_FOUND);
			}else {
			    throw new RuntimeException(body.toString());
			}
			
		}
	}

}
