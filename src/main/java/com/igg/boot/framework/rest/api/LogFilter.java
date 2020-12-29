package com.igg.boot.framework.rest.api;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jboss.logging.MDC;

public class LogFilter implements Filter {
    public static final String REQUEST_ID = "requestId";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String requestId = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, requestId);
        try {
            chain.doFilter(request, response);
        }finally {
            MDC.remove(REQUEST_ID);
        }
        
    }

    @Override
    public void destroy() {
        
    }

}
