package com.danran.common.api.order;

import com.danran.common.domain.Order;

import java.sql.SQLException;

/**
 * @Classname OrderServiceApi
 * @Description TODO
 * @Date 2021/8/1 9:41
 * @Created by ASUS
 */
public interface OrderServiceApi {
    /***
     *
     * @param orderID 订单ID
     * @param userId 用户id
     * @param bookId the id of the book
     * @param amount amount
     * @return 响应数据
     * @throws SQLException sql异常
     */
    Order createOrder(String orderID, int userId, String bookId, int amount) throws SQLException;

    /***
     * 根据order的ID来获取订单信息
     * @param orderID the id of the order
     * @return order
     * @throws SQLException if any sql exceptions occurs
     */
    Order getOrderById(String orderID) throws SQLException;

    Order getOrderByUserIDAndBookID(int userID, String bookID) throws SQLException;
}
