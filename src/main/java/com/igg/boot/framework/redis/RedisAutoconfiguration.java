package com.igg.boot.framework.redis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;


@Configuration
@ConditionalOnClass(JedisPool.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoconfiguration {
	public static final String NX = "NX";
	public static final String EX = "EX";
	@Autowired
	private RedisProperties redisProperties;
	   
	@Bean
	public GenericObjectPoolConfig poolConfig() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(redisProperties.getMaxIdle());
		poolConfig.setMaxTotal(redisProperties.getMaxTotal());
		poolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
		poolConfig.setMinEvictableIdleTimeMillis(redisProperties.getMinEvictableIdleTimeMills());
		poolConfig.setNumTestsPerEvictionRun(redisProperties.getNumTestsPerEvictionRun());
		poolConfig.setTestOnBorrow(redisProperties.isTestOnBorrow());
		poolConfig.setTestOnReturn(redisProperties.isTestOnReturn());
		poolConfig.setTestWhileIdle(redisProperties.isTestWhileIdle());
		poolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getTimeBetweenEvictionRunsMillis());
		
		return poolConfig;
	}
	
	@Bean
    @ConditionalOnClass(JedisPool.class)
    @ConditionalOnProperty(prefix = "igg.redis",name = "port", matchIfMissing = false)
    public JedisPool jedisPool(GenericObjectPoolConfig poolConfig) {
        if(StringUtils.isEmpty(redisProperties.getPassword())) {
            return new JedisPool(poolConfig,redisProperties.getIp(),redisProperties.getPort(),redisProperties.getTimeout());
        }else {
            return new JedisPool(poolConfig,redisProperties.getIp(),redisProperties.getPort(),redisProperties.getTimeout(),redisProperties.getPassword());
        }
        
    }
    
    @Bean
    @ConditionalOnClass(JedisCluster.class)
    @ConditionalOnProperty(prefix = "igg.redis",name = "host", matchIfMissing = false)
    public JedisCluster jedisCluster(GenericObjectPoolConfig poolConfig) {
        List<String> lists = redisProperties.getHost();
        Set<HostAndPort> nodes = new HashSet<>();
        lists.forEach(list -> {
            String[] hostAndPort = list.split(":");
            HostAndPort node = new HostAndPort(hostAndPort[0],Integer.parseInt(hostAndPort[1]));
            nodes.add(node);
        });
        if(StringUtils.isEmpty(redisProperties.getPassword())){
            return new JedisCluster(nodes,redisProperties.getTimeout(),poolConfig);
        }else {
            return new JedisCluster(nodes,redisProperties.getTimeout(),redisProperties.getTimeout(),5,redisProperties.getPassword(),poolConfig);
        }
        
    }
    
    @Bean
    @ConditionalOnBean(JedisCluster.class)
    public JedisTemplate jedisClusterTemplate(JedisCluster jedisCluster) {
        return new JedisClusterTemplate(jedisCluster);
    }
    
    @Bean
    @ConditionalOnBean(JedisPool.class)
    public JedisTemplate jedisTemplate(JedisPool jedisPool) {
        RedisExecute redisExecute = new RedisExecute(jedisPool);
        
        return new JedisSingleTemplate(redisExecute);
    }
}
