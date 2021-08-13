package com.danran.cache.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Classname RedisPoolFactory
 * @Description TODO
 * @Date 2021/8/3 19:57
 * @Created by ASUS
 * <p>
 * jedis连接池
 * <p>
 * Jedis实例不是线程安全的,所以不可以多个线程共用一个Jedis实例，
 * 但是创建太多的实现也不好因为这意味着会建立很多sokcet连接。
 * JedisPool是一个线程安全的网络连接池。
 * 可以用JedisPool创建一些可靠Jedis实例，可以从池中获取Jedis实例，
 * 使用完后再把Jedis实例还回JedisPool。这种方式可以避免创建大量socket连接并且会实现高效的性能.
 */
@Component
public class RedisPoolFactory {

    @Autowired
    private RedisConfig redisConfig;

    @Bean
    public JedisPool jedisPoolFactory() {
        // jedis连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());// 配置最大连接数量
        poolConfig.setMaxWaitMillis(1000L * redisConfig.getPoolMaxWait()); // 转换为毫秒数
        return new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout());
    }

}
