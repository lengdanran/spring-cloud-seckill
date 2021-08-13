package com.danran.order.service;

import com.danran.common.api.order.OrderServiceApi;
import com.danran.common.domain.Order;
import com.danran.order.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * @Classname OrderService
 * @Description TODO
 * @Date 2021/7/31 23:50
 * @Created by ASUS
 */
@Service
public class OrderServiceImpl implements OrderServiceApi {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;


    /***
     * 首先向数据库中写入数据，然后将数据写到缓存中，这样可以保证缓存和数据库中的数据的一致
     * 1. 向 order_info 中插入订单详细信息
     * 2. 向 seckill_order 中插入订单概要
     *   两个操作需要构成一个数据库事务
     * @param orderID 订单ID
     * @param userId 用户id
     * @param bookId the id of the book
     * @param amount amount
     * @return 响应数据
     * @throws SQLException sql异常
     */
    @Transactional
    @Override
    public Order createOrder(String orderID, int userId, String bookId, int amount) throws SQLException {
        Order order = new Order();
        order.setId(orderID);
        order.setBookId(bookId);
        order.setUserId(userId);
        order.setAmount(amount);
        order.setStatus(0);
        // 将订单信息插入 order 表中
        orderMapper.insert(order);
        log.info("插入一条订单信息到order表中：" + order);
        // 将订单信息插入到Redis中
        return order;
    }

    /***
     * 根据order的ID来获取订单信息
     * @param orderID the id of the order
     * @return order
     * @throws SQLException if any sql exceptions occurs
     */
    @Override
    public Order getOrderById(String orderID) throws SQLException {
        return orderMapper.selectByPrimaryKey(orderID);
    }

    @Override
    public Order getOrderByUserIDAndBookID(int userID, String bookID) throws SQLException {
        return orderMapper.getOrderByUserIDAndBookID(userID, bookID);
    }
}
