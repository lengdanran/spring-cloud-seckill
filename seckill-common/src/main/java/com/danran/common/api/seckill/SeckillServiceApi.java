package com.danran.common.api.seckill;

import com.danran.common.domain.Order;

/**
 * @Classname SeckillServiceApi
 * @Description TODO
 * @Date 2021/8/5 13:24
 * @Created by ASUS
 *
 * 秒杀服务接口类
 */
public interface SeckillServiceApi {
    /***
     * 执行秒杀操作，包含以下两个步骤
     * 1. 从book表中扣减库存
     * 2. 将生成的订单信息写入到order表中
     * @param userID 秒杀的用户id
     * @param bookID 书籍的ID
     * @return 订单信息对象
     */
    Order seckill(int userID, String bookID);

    /***
     * 获取秒杀的结果
     * @param userID 秒杀的用户id
     * @param bookID 书籍的ID
     * @return long
     */
    Order getSeckillResult(int userID, String bookID);
}
