package com.igg.boot.framework.apollo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.igg.boot.framework.autoconfigure.ApplicationContextHolder;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnClass(value = {ConfigChangeListener.class,RefreshScope.class})
@Configuration
@Slf4j
public class ApolloAutoConfiguration {
    
    @Bean
    public ConfigRefreshListen configRefreshListion(RefreshScope refreshScope) {
        return new ConfigRefreshListen(refreshScope);
    }
      
    private class ConfigRefreshListen {
        private RefreshScope refreshScope;
        
        public ConfigRefreshListen(RefreshScope refreshScope) {
            this.refreshScope = refreshScope;
        }
        
        @ApolloConfigChangeListener(value=  { ConfigConsts.NAMESPACE_APPLICATION, "application.yml" })
        public void onChange(ConfigChangeEvent changeEvent) {
            log.info("=========start refresh config==========");
            refreshProperties(changeEvent);
            log.info("=========end refresh config=============");
        }

        public void refreshProperties(ConfigChangeEvent changeEvent) {
            ApplicationContextHolder.getContext().publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
            refreshScope.refreshAll();
        }
    }
}
