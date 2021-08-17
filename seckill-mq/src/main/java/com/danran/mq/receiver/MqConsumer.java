package com.danran.mq.receiver;

import cn.hutool.http.HttpUtil;
import com.danran.common.api.mq.SkMessage;
import com.danran.common.domain.Book;
import com.danran.common.domain.Order;
import com.danran.common.domain.User;
import com.danran.common.service.URLService;
import com.danran.mq.config.MQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname MqConsumer
 * @Description TODO
 * @Date 2021/8/8 13:58
 * @Created by ASUS
 *
 * MQ消息的消费者
 * 消费者绑定队列监听，接收来自队列中的消息
 */
@Service
public class MqConsumer {
    private static final Logger log = LoggerFactory.getLogger(MqConsumer.class);

    @Autowired
    private URLService urlService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiveSkInfo(SkMessage msg) {
        log.info("MQ 收到一条消息：" + msg);
        // get the user and book information in the msg
        User user = msg.getUser();
        Book book = msg.getBook();
        if (urlService.lock(book.getId(), book.getId(), 200000)) {
            // get the stock of the book
            log.info("Lock the book == " + book);
            int stock = urlService.getBookStockByBookId(book.getId());
            if (stock <= 0) {
                log.info("库存不足，扣减库存失败。");
                return;
            }
            // 判断是否已经秒杀到了（保证秒杀接口幂等性）
            Order seckill_order = this.getSkOrderByUserIdAndGoodsId(user.getId(), book.getId());
            if (seckill_order != null) {
                log.info("该用户已经秒杀得到book" + book + "的订单");
                return;
            }
            // 访问秒杀服务接口
            urlService.seckill(user, book);
            urlService.unlock(book.getId(), book.getId());
            log.info("Unlock the book == " + book);
        }
    }

    public static void main(String[] args) {
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("book_id", "thebookid");
        map.put("user_id", 20);
        String stock = HttpUtil.get("http://localhost:8082/mq/test", map);
        System.out.println(stock);
    }

    /***
     * 过用户id与商品id从订单列表中获取订单信息，这个地方用了唯一索引（unique index!!!!!）
     * <p>
     * 优化，不同每次都去数据库中读取秒杀订单信息，而是在第一次生成秒杀订单成功后，
     * 将订单存储在redis中，再次读取订单信息的时候就直接从redis中读取
     */
    private Order getSkOrderByUserIdAndGoodsId(int userId, String bookId) {
        // 先到redis缓冲中查询订单信息
        Order order = urlService.getRedisOrderByUserIdAndBookId(userId, bookId);
        if (order != null) {
            log.info("已经存在用户：" + userId + " 的 book : " + bookId + " 的订单");
            return order;
        }
        return urlService.getOrderFromDBByUserIdAndBookId(userId, bookId);
    }
}
