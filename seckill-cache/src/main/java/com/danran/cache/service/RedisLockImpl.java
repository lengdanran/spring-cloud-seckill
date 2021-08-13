package com.danran.cache.service;

import com.danran.common.api.cache.DLockApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * @Classname RedisLockImpl
 * @Description TODO
 * @Date 2021/8/3 20:12
 * @Created by ASUS
 */
@Service
public class RedisLockImpl implements DLockApi {

    /**
     * 通过连接池对象可以获得对redis的连接
     */
    @Autowired
    private JedisPool jedisPool;

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    private static final Logger log = LoggerFactory.getLogger(RedisLockImpl.class);



    /**
     * 获取锁
     *
     * @param lockKey     锁
     * @param uniqueValue 能够唯一标识请求的值，以此保证锁的加解锁是同一个客户端
     * @param expireTime  过期时间, 单位：milliseconds
     * @return 是否获得锁
     */
    @Override
    public boolean lock(String lockKey, String uniqueValue, int expireTime) {
        try (Jedis jedis = jedisPool.getResource()) {// jedis操作完成之后会调用.close()方法，进而归还连接到连接池中去
            // get a jedis instance from the pool
            // try to get the lock
            String result = jedis.set(lockKey, uniqueValue, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            return LOCK_SUCCESS.equals(result);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 使用Lua脚本保证解锁的原子性
     *
     * @param lockKey     锁
     * @param uniqueValue 能够唯一标识请求的值，以此保证锁的加解锁是同一个客户端
     * @return 是否释放锁
     */
    @Override
    public boolean unlock(String lockKey, String uniqueValue) {
        try (Jedis jedis = jedisPool.getResource()) {// jedis操作完成之后会调用.close()方法，进而归还连接到连接池中去
            // 使用Lua脚本保证操作的原子性
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del', KEYS[1]) " +
                    "else return 0 " +
                    "end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(uniqueValue));
            return RELEASE_SUCCESS.equals(result);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }
}
