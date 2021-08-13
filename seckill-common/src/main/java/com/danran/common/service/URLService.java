package com.danran.common.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.danran.common.api.cache.vo.BookKeyPrefix;
import com.danran.common.api.cache.vo.OrderKeyPrefix;
import com.danran.common.api.cache.vo.SkKeyPrefix;
import com.danran.common.api.mq.SkMessage;
import com.danran.common.domain.Book;
import com.danran.common.domain.Order;
import com.danran.common.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname URLService
 * @Description TODO
 * @Date 2021/8/9 14:06
 * @Created by ASUS
 */
public class URLService {

    private static final Logger logger = LoggerFactory.getLogger(URLService.class);

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /*************Redis缓存服务调用*****************/
    /***
     *
     * @param keyPrefix
     * @param bookId
     * @param value
     * @param <T>
     */
    public <T> void cacheSet(String keyPrefix, String bookId, T value) {
//        String url = "http://redis-service/cache/set";
        ServiceInstance serviceInstance = loadBalancerClient.choose("redis-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/cache/set";
        logger.info("post : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("keyPrefix", keyPrefix);
        params.put("book_id", bookId);
        params.put("value", JSON.toJSONString(value));
        HttpUtil.post(url, params);
    }

    /***
     *
     * @param keyPrefix
     * @param key
     * @return
     */
    public Boolean cacheExists(String keyPrefix, String key) {
//        String url = "http://redis-service/cache/exists";
        ServiceInstance serviceInstance = loadBalancerClient.choose("redis-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/cache/exists";
        logger.info("get : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>(2);
        params.put("keyPrefix", keyPrefix);
        params.put("key", key);
        String s = HttpUtil.get(url, params);
        try {
            return JSON.parseObject(s, Boolean.class);
        } catch (Exception e) {
            logger.info(e.toString());
            e.printStackTrace();
            return false;
        }
    }

    /***
     *
     * @param userId
     * @param bookId
     * @return
     */
    public Order getRedisOrderByUserIdAndBookId(int userId, String bookId) {
//        String url = "http://redis-service/cache/get";
//        redisService.get(OrderKeyPrefix.SK_ORDER, ":" + userId + "_" + goodsId, SeckillOrder.class);
        ServiceInstance serviceInstance = loadBalancerClient.choose("redis-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/cache/get";
        logger.info("get : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("KeyPrefix", "SK_ORDER");
        params.put("user_id", userId);
        params.put("book_id", bookId);
//        params.put("clazz", Order.class);
        String jsonStr = HttpUtil.get(url, params);
        logger.info("jsonStr=" + jsonStr);
        return jsonStr.equals("null") ? null : JSON.parseObject(jsonStr, Order.class);
    }

    /***
     *
     * @param keyPrefix
     * @param bookId
     * @return
     */
    public Integer cacheDecr(String keyPrefix, String bookId) {
//        String url = "http://redis-service/cache/decr";
        ServiceInstance serviceInstance = loadBalancerClient.choose("redis-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/cache/decr";
        logger.info("post : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>(2);
        params.put("keyPrefix", keyPrefix);
        params.put("book_id", bookId);
        String post = HttpUtil.post(url, params);
        try {
            return Integer.valueOf(post);
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            return -1;
        }
    }

    /*****************订单服务调用*******************/
    /***
     *
     * @param orderID
     * @param userId
     * @param bookId
     * @param amount
     * @return
     */
    public Order createOrder(String orderID, int userId, String bookId, int amount) {
//        String url = "http://order-service/order/create_order";
        ServiceInstance serviceInstance = loadBalancerClient.choose("order-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/order/create_order";
        logger.info("post : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("order_id", orderID);
        params.put("user_id", userId);
        params.put("book_id", bookId);
        params.put("amount", amount);
        String jsonStr = HttpUtil.post(url, params);
        return JSON.parseObject(jsonStr, Order.class);
    }

    /***
     *
     * @param userId
     * @param bookId
     * @return
     */
    public Order getOrderFromDBByUserIdAndBookId(int userId, String bookId) {
//        String url = "http://order-service/order/get_order_by_userid_bookid";
        ServiceInstance serviceInstance = loadBalancerClient.choose("order-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/order/get_order_by_userid_bookid";
        logger.info("get : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("user_id", userId);
        params.put("book_id", bookId);
        String jsonStr = HttpUtil.get(url, params);
        return jsonStr.equals("null") ? null : JSON.parseObject(jsonStr, Order.class);
    }

    /********************用户服务调用*******************/
    /***
     *
     * @param userId
     * @return
     */
    public Boolean isValidUser(int userId) {
//        String url = "http://user-service/user/is_valid_user";
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/user/is_valid_user";
        logger.info("get : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>(1);
        params.put("user_id", userId);
        String s = HttpUtil.get(url, params);
        return s.equals("true");
    }

    /***
     *
     * @param userId
     * @return
     */
    public User getUserById(int userId) {
//        String url = "http://user-service/user/get_user_by_id";
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/user/get_user_by_id";
        logger.info("get : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>(1);
        params.put("user_id", userId);
        String s = HttpUtil.get(url, params);
        try {
            return JSON.parseObject(s, User.class);
        } catch (Exception e) {
            logger.info(e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**********************书籍服务调用*****************/

    /***
     * 根据书籍的id到书籍服务中查询库存信息
     * http://book-service/book/get_stock
     * @param bookId the id of the book
     * @return stock
     */
    public Integer getBookStockByBookId(String bookId) {
//        String url = "http://book-service/book/get_stock";
        ServiceInstance serviceInstance = loadBalancerClient.choose("book-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/book/get_stock";
        logger.info("get : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("book_id", bookId);
        return Integer.parseInt(HttpUtil.get(url, params));
    }

    /***
     *
     * @param user
     * @param book
     * @return
     */
    public Order seckill(User user, Book book) {
//        String url = "http://book-service/book/seckill";
        ServiceInstance serviceInstance = loadBalancerClient.choose("book-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/book/seckill";
        logger.info("post : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("user", JSON.toJSONString(user));
        params.put("book", JSON.toJSONString(book));
        String jsonStr = HttpUtil.post(url, params);
        System.out.println("------------" + jsonStr);
        return JSON.parseObject(jsonStr, Order.class);
    }

    /***
     *
     * @param bookId
     * @return
     */
    public Book getBookById(String bookId) {
//        String url = "http://book-service/book/get_book_by_id";
        ServiceInstance serviceInstance = loadBalancerClient.choose("book-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/book/get_book_by_id";
        logger.info("get : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>(1);
        params.put("book_id", bookId);
        String s = HttpUtil.get(url, params);
        try {
            return JSON.parseObject(s, Book.class);
        } catch (Exception e) {
            logger.info(e.toString());
            e.printStackTrace();
            return null;
        }
    }
    /***************消息队列服务调用**************/
    /***
     *
     * @param message
     * @return
     */
    public Boolean sendMQMessage(SkMessage message) {
//        String url = "http://mq-service/mq/send_message";
        ServiceInstance serviceInstance = loadBalancerClient.choose("mq-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/mq/send_message";
        logger.info("post : " + url);
        Map<String, Object> params = new ConcurrentHashMap<>(1);
        params.put("message", JSON.toJSONString(message));
        String post = HttpUtil.post(url, params);
        try {
            return JSON.parseObject(post, Boolean.class);
        } catch (Exception e) {
            logger.info(e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
