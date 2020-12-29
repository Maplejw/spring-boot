package com.igg.boot.framework.autoconfigure;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import com.igg.boot.framework.exception.BindExceptionCode;
import com.igg.boot.framework.exception.FrameworkSystemException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;
import com.igg.boot.framework.rest.api.DefaultErrorResourceView;
import com.igg.boot.framework.rest.api.LogFilter;
import com.igg.boot.framework.rest.api.RestResponse;
import com.igg.boot.framework.rest.api.RestResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IggHandleException extends AbstractHandlerMethodExceptionResolver {
    private HttpMessageConverter<?> messageConverter;
    private View view;

    public IggHandleException(HttpMessageConverter<?> messageConverter) {
        this.messageConverter = messageConverter;
    }

    public IggHandleException(HttpMessageConverter<?> messageConverter, View view) {
        this.messageConverter = messageConverter;
        this.view = view;
    }

    @SuppressWarnings({ "unchecked", "resource" })
	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception ex) {
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();

		MediaType.sortByQualityValue(acceptedMediaTypes);
		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}

		RestResponse<Object> restResponse = new RestResponse<>();
		if (ex instanceof FrameworkSystemException) {
			FrameworkSystemException wx = (FrameworkSystemException) ex;
			restResponse.setCode(wx.getCode());
			restResponse.setMessage(wx.getMessage());
			response.setStatus(wx.getHttpStatus().value());
		} else if(ex instanceof MethodArgumentNotValidException){
			MethodArgumentNotValidException bx = (MethodArgumentNotValidException) ex;
			restResponse.setCode(BindExceptionCode.ERROR_PARM.getCode());
			restResponse.setMessage(BindExceptionCode.ERROR_PARM.getMessage() + bx.getBindingResult().getFieldError().getDefaultMessage());
			response.setStatus(BindExceptionCode.ERROR_PARM.getHttpStatus().value());
		} else {
			restResponse.setCode(HttpSystemExceptionCode.SYSTEM_ERROR.getCode());
			restResponse.setMessage(HttpSystemExceptionCode.SYSTEM_ERROR.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		restResponse.setRequestId(MDC.get(LogFilter.REQUEST_ID));
		if(response.getStatus() ==  HttpStatus.INTERNAL_SERVER_ERROR.value()) {
		    log.error(ex.getMessage(),ex);
		} else {
		    log.warn(ex.getMessage(),ex);
		}
		
		if (handlerMethod.hasMethodAnnotation(RestResponseBody.class) || handlerMethod.getReturnType().getDeclaringClass().isAnnotationPresent(RestResponseBody.class)) {
			for (MediaType mediaType : acceptedMediaTypes) {
				if (messageConverter.canWrite(restResponse.getClass(), mediaType)) {
					try {
						((HttpMessageConverter<Object>) messageConverter).write(restResponse, mediaType, outputMessage);
						break;
					} catch (HttpMessageNotWritableException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return new ModelAndView();
		} else {
			ModelAndView error;
			if (view != null) {
				error = new ModelAndView(this.view);
			} else {
				error = new ModelAndView(new DefaultErrorResourceView());
			}

			error.addObject("code", restResponse.getCode());
			error.addObject("message", restResponse.getMessage());
			error.addObject("request_id",restResponse.getRequestId());
			return error;
		}
	}

}
