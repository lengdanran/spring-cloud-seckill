package com.danran.mq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


/**
 * @Classname MQConfig
 * @Description TODO
 * @Date 2021/8/8 10:59
 * @Created by ASUS
 * 通过配置文件获取消息队列
 */
@Configuration
public class MQConfig {
    /***
     * the name of the mq
     */
    public static final String SECKILL_QUEUE = "seckill.queue";

    /**
     * 秒杀 routing key, 生产者沿着 routingKey 将消息投递到 exchange 中
     */
    public static final String SK_ROUTING_KEY = "routing.sk";

    /**
     * Direct模式 交换机exchange
     * 生成用于秒杀的queue
     *
     * @return
     */
    @Bean
    public Queue seckillQueue() {
        return new Queue(SECKILL_QUEUE, true);
    }
    /**
     * 实例化 RabbitTemplate
     *
     * @param connectionFactory 连接工厂
     */
    @Bean
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
    }
}
