package com.igg.boot.framework.rest.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class ResponseMethodBodyHandler implements HandlerMethodReturnValueHandler, BeanPostProcessor {
	private HttpMessageConverter<?> messageConverter;

	public ResponseMethodBodyHandler(HttpMessageConverter<?> messageConverter) {
		this.messageConverter = messageConverter;
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.getDeclaringClass().isAnnotationPresent(RestResponseBody.class)
				|| returnType.getMethod().isAnnotationPresent(RestResponseBody.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);
		ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(
				webRequest.getNativeRequest(HttpServletRequest.class));
		ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(
				webRequest.getNativeResponse(HttpServletResponse.class));
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();

		MediaType.sortByQualityValue(acceptedMediaTypes);
		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}

		RestResponse<Object> restResponse = getRestResponse(returnValue, returnType);
		for (MediaType mediaType : acceptedMediaTypes) {
			if (messageConverter.canWrite(restResponse.getClass(), mediaType)) {
				try {
					((HttpMessageConverter<Object>) messageConverter).write(restResponse, mediaType, outputMessage);
					return;
				} catch (HttpMessageNotWritableException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private RestResponse<Object> getRestResponse(Object returnValue, MethodParameter returnType) {
		String value = "";
		if (returnType.getDeclaringClass().isAnnotationPresent(RestResponseBody.class)) {
			value = returnType.getDeclaringClass().getAnnotation(RestResponseBody.class).value();
		} else {
			value = returnType.getMethod().getAnnotation(RestResponseBody.class).value();
		}
		RestResponse<Object> restResponse = new RestResponse<>();
		Class<?> clz = returnValue.getClass();
		if (clz.equals(java.lang.Integer.class) || clz.equals(java.lang.Long.class)
				|| clz.equals(java.lang.String.class)
				|| clz.equals(java.lang.Double.class) || clz.equals(java.lang.Float.class)) {
			Map<String, Object> map = new HashMap<>(2);
			map.put(value, returnValue);
			restResponse.setData(map);
		} else {
			restResponse.setData(returnValue);
		}
		restResponse.setRequestId(MDC.get(LogFilter.REQUEST_ID));

		return restResponse;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RequestMappingHandlerAdapter) {
			List<HandlerMethodReturnValueHandler> handlers = ((RequestMappingHandlerAdapter) bean)
					.getReturnValueHandlers();
			List<HandlerMethodReturnValueHandler> codeHandlers = new ArrayList<HandlerMethodReturnValueHandler>();
			ResponseMethodBodyHandler handler = null;
			for (int i = 0; i < handlers.size(); i++) {
				HandlerMethodReturnValueHandler tmp = handlers.get(i);
				if (tmp instanceof ResponseMethodBodyHandler) {
					handler = (ResponseMethodBodyHandler) tmp;
					continue;
				}
				codeHandlers.add(tmp);
			}
	
			if (handler != null) {
				codeHandlers.add(0, handler);
				((RequestMappingHandlerAdapter) bean).setReturnValueHandlers(codeHandlers);
			}
		}
		return bean;
	}

}
