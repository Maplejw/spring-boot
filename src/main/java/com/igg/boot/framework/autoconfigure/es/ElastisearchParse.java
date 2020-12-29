package com.igg.boot.framework.autoconfigure.es;

import java.lang.reflect.Field;

import org.springframework.data.elasticsearch.annotations.Document;

import com.igg.boot.framework.autoconfigure.es.condition.exception.ElastisearchException;
import com.igg.boot.framework.exception.HttpSystemExceptionCode;

public class ElastisearchParse {
    
    public static Document getDoucment(Class<?> clz) {
        if(!clz.isAnnotationPresent(Document.class)) {
            throw new ElastisearchException(HttpSystemExceptionCode.ES_ANNOTATION_NOT_FOUND_ERROR);
        }
        Document document = (Document)clz.getAnnotation(Document.class);
        
        return document;
    }
    
    public static <T> String getRouting(T model) {
        Field[] fields = model.getClass().getDeclaredFields();
        Object routingName= "";
        for(Field field : fields) {
            if(field.isAnnotationPresent(Routing.class)) {
                try {
                    field.setAccessible(true);
                    routingName = field.get(model);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
        if(routingName == null) {
            throw new ElastisearchException(HttpSystemExceptionCode.ES_ANNOTATION_ROUTING_NOT_FOUND_ERROR);
        }
        
        return routingName + "";
    }
}
