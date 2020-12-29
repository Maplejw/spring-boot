package com.igg.boot.framework.autoconfigure.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DBTargetSource {
    String name() default DBDefaultAutoconfiguration.PRIMARY_DATASOURCE;
    
}
