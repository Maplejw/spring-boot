package com.igg.boot.framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.igg.boot.framework.redis.JedisTemplate;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTest {
    @Autowired
    private JedisTemplate redis;
    
    
    @Test
    public void setNx() {
        boolean flag = redis.setexnx("xxx", "1", 30);
        
        System.out.println(flag);
    }
    
   // @Test
    public void test() throws InterruptedException {
        int thread = 10;
        ExecutorService exec = Executors.newFixedThreadPool(thread);
        String key = "lock";
        
        for (int i = 0; i < thread; i++) {
            exec.execute(() -> {
                boolean flag = redis.setexnx(key, "1", 5);
                log.info(Thread.currentThread().getName() + ":" + flag);
            });
        }
        Thread.sleep(2000);
        exec.shutdown();
    }
    
   // @Test
    public void limit() throws InterruptedException {
        int thread = 5000;
        ExecutorService exec = Executors.newFixedThreadPool(thread);
        String key = "limit";
        int time = 1;
        int limit = 10;
        for (int i = 0; i < thread; i++) {
            exec.execute(() -> {
                long ret = redis.incr(key);
                if(ret == 1) {
                    redis.expire(key, time);
                }else if(ret > limit) {
                   log.info(Thread.currentThread().getName() + ": i am limit");
                }
              
            });
        }
        Thread.sleep(2000);
        exec.shutdown();
    }
}
