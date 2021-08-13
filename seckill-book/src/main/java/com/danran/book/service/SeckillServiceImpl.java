package com.danran.book.service;

import com.danran.common.api.book.BookServiceApi;
import com.danran.common.api.cache.vo.BookKeyPrefix;
import com.danran.common.api.seckill.SeckillServiceApi;
import com.danran.common.domain.Order;
import com.danran.common.service.URLService;
import com.danran.common.util.OrderIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Classname SeckillServiceImpl
 * @Description TODO
 * @Date 2021/8/5 14:11
 * @Created by ASUS
 *
 * 秒杀服务接口的实现类
 */
@Service
public class SeckillServiceImpl implements SeckillServiceApi {

    private static final Logger log = LoggerFactory.getLogger(SeckillServiceImpl.class);

    @Autowired
    private URLService urlService;

    @Autowired
    private BookServiceApi bookServiceApi;

    @Autowired
    private OrderIDUtil orderIDUtil;

    /***
     * 执行秒杀操作，包含以下两个步骤
     *
     * 减库存，生成订单，实现秒杀操作核心业务
     * 秒杀操作由两步构成，不可分割，为一个事务
     *
     * 1. 从book表中扣减库存
     * 2. 将生成的订单信息写入到order表中
     * @param userID 秒杀的用户id
     * @param bookID 书籍的ID
     * @return 订单信息对象
     */
    @Transactional
    @Override
    public Order seckill(int userID, String bookID) {
        // 减库存
        int stock;
        if ((stock = bookServiceApi.reduceBookStock(bookID)) < 0) {
            log.info("库存不足，秒杀失败");
            setBookOver(bookID);
            return null;
        }
        // 生成订单，往order表中写入订单信息
        log.info("开始创建订单,写入订单order数据表");
//            Order order = orderServiceApi.createOrder(orderIDUtil.getOrderID(), userID, bookID, 1);
        Order order = urlService.createOrder(orderIDUtil.getOrderID(), userID, bookID, 1);
        // 更新缓存中的信息
        log.info("更新缓存信息");
//            redisServiceApi.set(BookKeyPrefix.BOOK_STOCK, bookID, stock);
        urlService.cacheSet("bookStock-0", bookID, stock);
        log.info("订单号：[" + order.getId() + "] ===> 创建成功");
        return order;
    }

    /***
     * 设置缓存中的商品数量为0
     * @param bookID the id of the book
     */
    private void setBookOver(String bookID) {
//        redisServiceApi.set(SkKeyPrefix.BOOK_SK_OVER, bookID, true);
        urlService.cacheSet("bookStock-0", bookID, true);
    }

    /***
     * 获取秒杀的结果
     * @param userID 秒杀的用户id
     * @param bookID 书籍的ID
     * @return order
     */
    @Override
    public Order getSeckillResult(int userID, String bookID) {
        //            Order order = orderServiceApi.getOrderByUserIDAndBookID(userID, bookID);
        Order order = urlService.getRedisOrderByUserIdAndBookId(userID, bookID);
        log.info("获得订单信息：" + order);
        return order;
    }
}
