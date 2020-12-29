package com.igg.boot.framework.autoconfigure.db;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Aspect
@Order(1)
@Configuration
public class DynamicDatasourceAspect {
	@Before("@annotation(dbTargetSource)")
    public void changeDatasource(JoinPoint point, DBTargetSource dbTargetSource) {
        DataSourceHolder.setDatasource(dbTargetSource.name());
    }
    
    @After("@annotation(dbTargetSource)")
    public void clearDataSource(JoinPoint point, DBTargetSource dbTargetSource) {
        DataSourceHolder.removeDatasource();
    }
}
