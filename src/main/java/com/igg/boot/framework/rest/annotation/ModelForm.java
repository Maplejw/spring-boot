package com.igg.boot.framework.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.validation.annotation.Validated;


@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Validated
public @interface ModelForm {

}
