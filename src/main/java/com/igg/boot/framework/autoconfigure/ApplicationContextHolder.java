package com.igg.boot.framework.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext context = null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.context = applicationContext;
    }
    
    public static ApplicationContext getContext() { 
        return context;
    }
    
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
    
    public static Object getBean(Class<?> classType) {
        return context.getBean(classType);
    }
    
}

