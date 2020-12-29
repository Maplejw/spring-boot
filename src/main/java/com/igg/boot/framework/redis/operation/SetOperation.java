package com.igg.boot.framework.redis.operation;

import java.util.Optional;
import java.util.Set;

public interface SetOperation {
	void sadd(String key, String... values);
	
	long scard(String key);
	
	boolean sismember(String key, String value);
	
	Set<String> smembers(String key);
	
	long srem(String key, String... values);
	
	Optional<String> spop(String key);
	
	
	long zadd(String key,double score,String value);
	
	Set<String> zrange(String key,long start,long end);
	
	Set<String> zrangeByScore(String key, double min, double max);
	
	Set<String> zrangeByScore(String key, double min, double max, int offset, int count);
	
	long zrank(String key,String value);
	
	long zrevrank(String key, String value);
	
	long zrem(String key, String... values);
	
	long zremrangeByScore(String key, double min, double max);

    long zremrangeByRank(String key, long start, long end);
    
    Set<String> zrevrange(String key, long start, long end);
    
    Set<String> zrevrangeByScore(String key, double max, double min);

    Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count);
	
	long zcard(String key);
	
	long zcount(String key,double min,double max);
	
	double zscore(String key, String value);
	
	double zincrby(String key,double score,String member);
}
