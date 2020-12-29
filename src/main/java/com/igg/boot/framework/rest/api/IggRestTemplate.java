package com.igg.boot.framework.rest.api;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class IggRestTemplate extends RestTemplate implements IggRestOperation {

    public IggRestTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    @Override
    public <T> RestResponse<List<T>> getForListResult(String url, Class<T> responseType) {
        return getForListResult(url, responseType, null);
    }

    public <T> RestResponse<List<T>> getForListResult(String url, Class<T> responseType, Map<String, ?> param) {
        Type type = GenericaClassUtils.createRestType(List.class, responseType);
        ParameterizedTypeReference<RestResponse<List<T>>> typeRef = ParameterizedTypeReference.forType(type);
        ResponseEntity<RestResponse<List<T>>> responseFormEntity = exchange(url, HttpMethod.GET, null, typeRef,
                param);

        return responseFormEntity.getBody();
    }

    public <T> RestResponse<List<T>> postForListResult(String url, Class<T> responseType, Map<String, ?> param) {
        Type type = GenericaClassUtils.createRestType(List.class, responseType);
        ParameterizedTypeReference<RestResponse<List<T>>> typeRef = ParameterizedTypeReference.forType(type);
        ResponseEntity<RestResponse<List<T>>> responseFormEntity = exchange(url, HttpMethod.POST,
                new HttpEntity<Map<String, ?>>(param), typeRef);

        return responseFormEntity.getBody();
    }

    @Override
    public <T> RestResponse<T> getForResult(String url, Class<T> responseType) {
        return getForResult(url, responseType, null);
    }

    public <T> RestResponse<T> getForResult(String url, Class<T> responseType, Map<String, ?> param) {
        Type type = GenericaClassUtils.createRestType(responseType);
        ParameterizedTypeReference<RestResponse<T>> typeRef = ParameterizedTypeReference.forType(type);
        ResponseEntity<RestResponse<T>> responseFormEntity = exchange(url, HttpMethod.GET, null, typeRef, param);

        return responseFormEntity.getBody();
    }

    public <T> RestResponse<T> postForResult(String url, Class<T> responseType, Map<String, ?> param) {
        Type type = GenericaClassUtils.createRestType(responseType);
        ParameterizedTypeReference<RestResponse<T>> typeRef = ParameterizedTypeReference.forType(type);
        ResponseEntity<RestResponse<T>> responseFormEntity = exchange(url, HttpMethod.POST,
                new HttpEntity<Map<String, ?>>(param), typeRef);

        return responseFormEntity.getBody();
    }

    @Override
    public <T> RestResponse<List<T>> postForBodyListResult(String url, Class<T> responseType, Map<String, ?> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Type type = GenericaClassUtils.createRestType(List.class, responseType);
        ParameterizedTypeReference<RestResponse<List<T>>> typeRef = ParameterizedTypeReference.forType(type);
        ResponseEntity<RestResponse<List<T>>> responseFormEntity = exchange(url, HttpMethod.POST,
                new HttpEntity<Map<String, ?>>(param,headers), typeRef);

        return responseFormEntity.getBody();
    }

    @Override
    public <T> RestResponse<T> postForBodyResult(String url, Class<T> responseType, Map<String, ?> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Type type = GenericaClassUtils.createRestType(responseType);
        ParameterizedTypeReference<RestResponse<T>> typeRef = ParameterizedTypeReference.forType(type);
        ResponseEntity<RestResponse<T>> responseFormEntity = exchange(url, HttpMethod.POST,
                new HttpEntity<Map<String, ?>>(param,headers), typeRef);

        return responseFormEntity.getBody();
    }

}
