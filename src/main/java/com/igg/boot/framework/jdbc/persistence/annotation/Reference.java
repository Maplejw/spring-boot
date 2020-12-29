package com.igg.boot.framework.jdbc.persistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.igg.boot.framework.jdbc.persistence.Entity;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Reference {
	Class<? extends Entity> value() default Entity.class;

	String column() default "";
}