package com.igg.boot.framework.jdbc.persistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String value() default "";

	int type() default 0;

	int size() default 0;

	boolean isUnique() default false;

	boolean isNotNull() default false;

	boolean hasDefault() default false;
}