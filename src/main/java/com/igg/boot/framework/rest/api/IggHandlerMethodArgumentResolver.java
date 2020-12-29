package com.igg.boot.framework.rest.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.igg.boot.framework.exception.BindException;
import com.igg.boot.framework.exception.BindExceptionCode;
import com.igg.boot.framework.rest.annotation.ModelForm;

public class IggHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ModelForm.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		String name = Conventions.getVariableNameForParameter(parameter);
		Object attribute = handleParameterNames(parameter, webRequest);
		WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
		if (binder.getTarget() != null) {
			validateIfApplicable(binder, parameter);
			if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
				throw new BindException(BindExceptionCode.ERROR_PARM, binder.getBindingResult().getFieldError().getDefaultMessage());
			}
		}

		return attribute;
	}
	
	private Object handleParameterNames(MethodParameter parameter, NativeWebRequest webRequest) {
		MethodParameter nestedParameter = parameter.nestedIfOptional();
		Class<?> clazz = nestedParameter.getNestedParameterType();
		Object obj = null;
		try {
			obj = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
			for(Field field : fields) {
				String name = field.getName();
				String underScoreName = underscoreName(name);
				if(webRequest.getParameter(underScoreName) != null) {
					wrapper.setPropertyValue(name, webRequest.getParameter(underScoreName));
				}
			}
			
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		return obj;
	}
	
	/**
	 * Validate the model attribute if applicable.
	 * <p>The default implementation checks for {@code @javax.validation.Valid},
	 * Spring's {@link org.springframework.validation.annotation.Validated},
	 * and custom annotations whose name starts with "Valid".
	 * @param binder the DataBinder to be used
	 * @param parameter the method parameter declaration
	 */
	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation ann : annotations) {
			if (ann.annotationType().getSimpleName().startsWith("ModelForm")) {
				Object hints = AnnotationUtils.getValue(ann);
				Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
				binder.validate(validationHints);
				break;
			}
		}
	}
	
	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		return isBindExceptionRequired(parameter);
	}
	
	protected boolean isBindExceptionRequired(MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		return !hasBindingResult;
	}
	
	public static String underscoreName(String camelCaseName) {
		StringBuilder result = new StringBuilder();
		if (camelCaseName != null && camelCaseName.length() > 0) {
			result.append(camelCaseName.substring(0, 1).toLowerCase());
			for (int i = 1; i < camelCaseName.length(); i++) {
				char ch = camelCaseName.charAt(i);
				if (Character.isUpperCase(ch)) {
					result.append("_");
					result.append(Character.toLowerCase(ch));
				} else {
					result.append(ch);
				}
			}
		}
		return result.toString();
	}

}
