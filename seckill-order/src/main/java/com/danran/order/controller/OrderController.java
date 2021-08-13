package com.danran.order.controller;


import com.alibaba.fastjson.JSON;
import com.danran.common.api.order.OrderServiceApi;
import com.danran.common.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @Classname OrderController
 * @Description TODO
 * @Date 2021/8/1 7:26
 * @Created by ASUS
 */
@RestController
@RequestMapping(value = "/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderServiceApi orderServiceApi;

    @PostMapping("/create_order")
    public String createOrder(@RequestParam("order_id") String orderID,
                              @RequestParam("user_id") int userId,
                              @RequestParam("book_id") String bookId,
                              @RequestParam("amount") int amount) {
        try {
            return JSON.toJSONString(orderServiceApi.createOrder(orderID, userId, bookId, amount));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @GetMapping("/get_order_by_userid_bookid")
    public Order get_order_by_userid_bookid(@RequestParam("user_id") int userId,
                                            @RequestParam("book_id") String bookId) {
        logger.info("OrderController获得参数：user_id = " + userId + ", book_id = " + bookId);
        try {
            return orderServiceApi.getOrderByUserIDAndBookID(userId, bookId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
