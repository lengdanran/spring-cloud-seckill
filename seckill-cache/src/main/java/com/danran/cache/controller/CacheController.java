package com.danran.cache.controller;

import com.danran.common.api.cache.RedisServiceApi;
import com.danran.common.api.cache.vo.BookKeyPrefix;
import com.danran.common.api.cache.vo.OrderKeyPrefix;
import com.danran.common.api.cache.vo.SkKeyPrefix;
import com.danran.common.domain.Book;
import com.danran.common.domain.Order;
import com.danran.common.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname CacheController
 * @Description TODO
 * @Date 2021/8/8 19:24
 * @Created by ASUS
 */
@RestController
@RequestMapping("/cache")
public class CacheController {

    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    private RedisServiceApi redisService;

    @GetMapping("/get")
    public Order getRedisOrderByUserIdAndBookId(@RequestParam("KeyPrefix") String keyPrefix,
                                                @RequestParam("user_id") int userId,
                                                @RequestParam("book_id") String bookId) {
        logger.info("CacheController获得请求参数：KeyPrefix = " + keyPrefix + ",user_id = " + userId + ", book_id = " + bookId);
        return redisService.get("SECK_OVER", ":" + userId + "_" + bookId, Order.class);
    }

    @PostMapping("/set")
    public <T> Boolean set(@RequestParam("keyPrefix") String keyPrefix,
                       @RequestParam("book_id") String bookId,
                       @RequestParam("value") String value) {
        redisService.set(keyPrefix, bookId, value);
        return true;
    }

    @GetMapping("/exists")
    public Boolean cacheExists(@RequestParam("keyPrefix") String keyPrefix,
                               @RequestParam("key") String key) {
        return redisService.exists(keyPrefix, key);
    }

    @PostMapping("/decr")
    public Integer cacheDecr(@RequestParam("keyPrefix") String keyPrefix,
                             @RequestParam("book_id") String bookId) {
        return Math.toIntExact(redisService.decr(keyPrefix, bookId));
    }

    @PostMapping("/lock")
    public Boolean lock(@RequestParam("key") String key,
                        @RequestParam("value") String value,
                        @RequestParam("time") String time) {
        int expireTime = Integer.parseInt(time);
        return redisService.lock(key, value, expireTime);
    }

    @PostMapping("/unlock")
    public void unlock(@RequestParam("key") String key,
                       @RequestParam("value") String value) {
        redisService.unlock(key, value);
    }

}
