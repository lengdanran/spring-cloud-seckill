package com.danran.cache.service;

import com.alibaba.fastjson.JSON;
import com.danran.common.api.cache.DLockApi;
import com.danran.common.api.cache.RedisServiceApi;
import com.danran.common.api.cache.vo.KeyPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Classname RedisServiceImpl
 * @Description TODO
 * @Date 2021/8/4 13:52
 * @Created by ASUS
 */
@Service
public class RedisServiceImpl implements RedisServiceApi {

    private static final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private DLockApi redisLock;

    /**
     * redis 的get操作，通过key获取存储在redis中的对象
     *
     * @param prefix key的前缀
     * @param key    业务层传入的key
     * @param clazz  存储在redis中的对象类型（redis中是以字符串存储的）
     * @return 存储于redis中的对象
     */
    @Override
    public <T> T get(String prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正用于存储在redis中的key
            String realKey = prefix + key; // 前缀和业务层面的key进行拼接
            // 通过key获取存储于redis中的对象（这个对象是以json格式存储的，所以是字符串）
            String jsonString = jedis.get(realKey);
            return stringToBean(jsonString, clazz); // 将json字符串转换为对应的对象
        } catch (Exception e) {
            log.error("RedisServer Exception : [get()] : " + e.toString());
            e.printStackTrace();
        } finally {
            returnToPool(jedis);
        }
        return null;
    }

    /**
     * redis的set操作
     *
     * @param keyPrefix 键的前缀
     * @param key    键
     * @param value  值
     * @return 操作成功为true，否则为true
     */
    @Override
    public <T> boolean set(String keyPrefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 首先拼接真正的key
            String realKey = keyPrefix + key;
            // 将对象序列化为json字符串
            String jsonString = beanToString(value);
            // 判断jsonString是否有效
            if (jsonString == null || jsonString.length() == 0) return false;
            // 获取key的过期时间
            int expire = Integer.parseInt(keyPrefix.split("-")[1]);
            if (expire <= 0) {
                // 设置默认过期时间(由redis自己的缓存策略来控制)
                jedis.set(realKey, jsonString);
            } else {
                // 设置为给定的时间
                jedis.setex(realKey, expire, jsonString);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisServer Exception : [set()] : " + e.toString());
            e.printStackTrace();
        } finally {
            returnToPool(jedis);
        }
        return false;
    }

    /**
     * 判断key是否存在于redis中
     *
     * @param keyPrefix key的前缀
     * @param key       业务层传过来的key
     * @return boolean
     */
    @Override
    public boolean exists(String keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(keyPrefix + key);
        } catch (Exception e) {
            log.error("RedisServer Exception : [set()] : " + e.toString());
            e.printStackTrace();
        } finally {
            returnToPool(jedis);
        }
        return false;
    }

    /**
     * 自增
     * <p>
     * 将存储在键上的数字加1。
     * 如果该键不存在或包含错误类型的值，则将该键设置为之前的值“0”，执行递增操作。
     * INCR命令被限制为64位有符号整数。
     * <p>
     * 注意:这实际上是一个字符串操作，也就是说，在Redis中没有“整数”类型。简单地说，
     * 存储在键上的字符串将被解析为以10为基数的64位有符号整数，递增，然后转换回字符串。
     *
     * @param keyPrefix key的前缀
     * @param key       业务层传过来的key
     * @return long
     */
    @Override
    public long incr(String keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(keyPrefix + key);
        } catch (Exception e) {
            log.error("RedisServer Exception : [incr()] : " + e.toString());
            e.printStackTrace();
        } finally {
            returnToPool(jedis);
        }
        return 0;
    }

    /**
     * 自减
     *
     * @param keyPrefix key的前缀
     * @param key       业务层传过来的key
     * @return long
     */
    @Override
    public long decr(String keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(keyPrefix + key);
        } catch (Exception e) {
            log.error("RedisServer Exception : [incr()] : " + e.toString());
            e.printStackTrace();
        } finally {
            returnToPool(jedis);
        }
        return 0;
    }

    /**
     * 删除缓存中的用户数据
     *
     */
    @Override
    public boolean delete(String keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(keyPrefix + key) > 0;
        } catch (Exception e) {
            log.error("RedisServer Exception : [incr()] : " + e.toString());
            e.printStackTrace();
        } finally {
            returnToPool(jedis);
        }
        return false;
    }

    /**
     * 将对象转换为对应的json字符串
     *
     * @param value 对象
     * @param <T>   对象的类型
     * @return 对象对应的json字符串
     */
    public static <T> String beanToString(T value) {
        if (value == null) return null;

        Class<?> clazz = value.getClass();
        /*首先对基本类型处理*/
        if (clazz == Integer.class) return "" + value;
        else if (clazz == Long.class) return "" + value;
        else if (clazz == String.class) return (String) value;
            /*然后对Object类型的对象处理*/
        else return JSON.toJSONString(value);
    }


    /**
     * 根据传入的class参数，将json字符串转换为对应类型的对象
     *
     * @param strValue json字符串
     * @param clazz    类型
     * @param <T>      类型参数
     * @return json字符串对应的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T stringToBean(String strValue, Class<T> clazz) {

        if ((strValue == null) || (strValue.length() <= 0) || (clazz == null))
            return null;

        // int or Integer
        if ((clazz == int.class) || (clazz == Integer.class)) return (T) Integer.valueOf(strValue);
            // long or Long
        else if ((clazz == long.class) || (clazz == Long.class)) return (T) Long.valueOf(strValue);
            // String
        else if (clazz == String.class) return (T) strValue;
            // 对象类型
        else return JSON.toJavaObject(JSON.parseObject(strValue), clazz);
    }

    /**
     * 将redis连接对象归还到redis连接池
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null) jedis.close();
    }

    public Boolean lock(String key, String value, int time) {
        return redisLock.lock(key, value, time);
    }

    public void unlock(String key, String value) {
        redisLock.unlock(key, value);
    }

}
