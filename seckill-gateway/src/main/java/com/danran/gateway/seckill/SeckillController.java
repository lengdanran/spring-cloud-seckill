package com.danran.gateway.seckill;

import com.danran.common.api.cache.vo.BookKeyPrefix;
import com.danran.common.api.cache.vo.SkKeyPrefix;
import com.danran.common.api.mq.SkMessage;
import com.danran.common.domain.Order;
import com.danran.common.result.CodeMsg;
import com.danran.common.result.Result;
import com.danran.common.service.URLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname SeckillController
 * @Description TODO
 * @Date 2021/8/9 13:09
 * @Created by ASUS
 */
@RestController
@RequestMapping("/gateway")
public class SeckillController {

    private static final Logger logger = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    private URLService urlService;

    /*用于内存标记，标记库存是否为空，从而减少对redis的访问*/
    private Map<String, Boolean> localOverMap = new ConcurrentHashMap<>();

    @PostMapping("/seckill")
    public Result<Integer> doSeckill(@RequestParam("user_id") int userId,
                                     @RequestParam("book_id") String bookId) {
        // 验证用户信息
        logger.info("验证用户信息");
        if (!urlService.isValidUser(userId)) {
            logger.info("非法用户，秒杀失败");
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        // 通过内存标记，减少对redis的访问，秒杀未结束才继续访问redis
        Boolean over = localOverMap.get(bookId);
        if (over != null && over) {
            logger.info("商品秒杀完了，来晚了");
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        // 预减库存，同时在库存为0时标记该商品已经结束秒杀
//        Integer stock = urlService.cacheDecr("bookStock", bookId);
//        if (stock < 0) {
//            localOverMap.put(bookId, true);// 秒杀结束。标记该商品已经秒杀结束
//            return Result.error(CodeMsg.SECKILL_OVER);
//        }
        // 判断是否重复秒杀
        // 从redis中取缓存，减少数据库的访问
        Order order = urlService.getRedisOrderByUserIdAndBookId(userId, bookId);
        if (order == null) {
            order = urlService.getOrderFromDBByUserIdAndBookId(userId, bookId);
        }
        if (order != null) return Result.error(CodeMsg.REPEATE_SECKILL);

        // 商品有库存且用户为秒杀商品，则将秒杀请求放入MQ
        SkMessage message = new SkMessage();
        message.setUser(urlService.getUserById(userId));
        message.setBook(urlService.getBookById(bookId));

        // 放入MQ(对秒杀请求异步处理，直接返回)
        urlService.sendMQMessage(message);
        // 排队中
        return Result.success(0);
    }

    @GetMapping("/get_result")
    public Result<Integer> getSeckillResult(@RequestParam("user_id") int userId,
                                            @RequestParam("book_id") String bookId) {
        Order order = urlService.getOrderFromDBByUserIdAndBookId(userId, bookId);
        if (order != null) {//秒杀成功
            return Result.success(1);
        } else {
            boolean isOver = getBookOver(bookId);
            if (isOver) {
                return Result.success(-1);
            } else {
                return Result.success(0);
            }
        }
    }

    private boolean getBookOver(String bookId) {
        return urlService.cacheExists("bookSkOver-0", bookId);
    }

}
