package com.igg.boot.framework.autoconfigure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.igg.boot.framework.rest.api.BasicErrorDefaultController;
import com.igg.boot.framework.rest.api.DefaultErrorResourceView;
import com.igg.boot.framework.rest.api.IggGsonHttpMessageConverters;
import com.igg.boot.framework.rest.api.IggHandlerMethodArgumentResolver;
import com.igg.boot.framework.rest.api.IggRestTemplate;
import com.igg.boot.framework.rest.api.LogFilter;
import com.igg.boot.framework.rest.api.ResponseMethodBodyHandler;
import com.igg.boot.framework.rest.api.RestConfiguration;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

@Configuration
@EnableConfigurationProperties(RestConfiguration.class)
public class IggMVCConfiguration implements WebMvcConfigurer {
    private final List<WebMvcConfigurer> delegates = new ArrayList<WebMvcConfigurer>();
    @Autowired
    private RestConfiguration restConfiguration;

    @Bean
    public ResponseMethodBodyHandler responseMethodBody() {
        return new ResponseMethodBodyHandler(fetchMessageConverter());
    }

    @Bean
    public ApplicationContextHolder getApplicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(new ResponseMethodBodyHandler(fetchMessageConverter()));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, fetchMessageConverter());
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.configureMessageConverters(converters);
        }
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new IggHandleException(fetchMessageConverter()));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new IggHandlerMethodArgumentResolver());
    }

    @Bean
    public FilterRegistrationBean<LogFilter> logFilterRegistration() {
        FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogFilter());
        registration.addUrlPatterns("/*");
        registration.setName("logFilter");
        registration.setOrder(1);

        return registration;
    }

    private HttpMessageConverter<?> fetchMessageConverter() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>(1);

        ClassLoader classLoader = IggMVCConfiguration.class.getClassLoader();
        boolean gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
        if (gsonPresent) {
            messageConverters.add(new IggGsonHttpMessageConverters());
        } else {
            messageConverters.add(mappingJackson2HttpMessageConverter());
        }

        return messageConverters.get(0);
    }
    
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);     
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    public BasicErrorDefaultController basicErrorController(ErrorAttributes errorAttributes) {
        return new BasicErrorDefaultController(errorAttributes);
    }

    @Bean("error")
    public View getDefaultView() {
        return new DefaultErrorResourceView();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public IggRestTemplate restTemplate() {
        IggRestTemplate iggRestTemplate = new IggRestTemplate(factory());

        List<HttpMessageConverter<?>> messageConverters = iggRestTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof MappingJackson2HttpMessageConverter
                    || converter instanceof GsonHttpMessageConverter) {
                iterator.remove();
            }
        }
        messageConverters.add(fetchMessageConverter());
        return iggRestTemplate;
    }

    private OkHttp3ClientHttpRequestFactory factory() {
        ConnectionPool okhttpPool = new ConnectionPool(restConfiguration.getMaxIdleConnect(),
                restConfiguration.getKeepAliveDuration(), TimeUnit.MINUTES);

        Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.connectTimeout(restConfiguration.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(restConfiguration.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(restConfiguration.getWriteTimeout(), TimeUnit.MILLISECONDS).connectionPool(okhttpPool)
                .build();

        return new OkHttp3ClientHttpRequestFactory(client);
    }

}
