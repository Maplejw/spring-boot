package com.igg.boot.framework.rest.api;

import java.util.List;
import java.util.Map;

public interface IggRestOperation {
    <T> RestResponse<List<T>> getForListResult(String url, Class<T> responseType, Map<String, ?> param);

    <T> RestResponse<List<T>> getForListResult(String url, Class<T> responseType);

    <T> RestResponse<List<T>> postForListResult(String url, Class<T> responseType, Map<String, ?> param);
    
    <T> RestResponse<List<T>> postForBodyListResult(String url, Class<T> responseType, Map<String, ?> param);

    <T> RestResponse<T> getForResult(String url, Class<T> responseType);

    <T> RestResponse<T> getForResult(String url, Class<T> responseType, Map<String, ?> param);

    <T> RestResponse<T> postForResult(String url, Class<T> responseType, Map<String, ?> param);
    
    <T> RestResponse<T> postForBodyResult(String url, Class<T> responseType, Map<String, ?> param);
}
