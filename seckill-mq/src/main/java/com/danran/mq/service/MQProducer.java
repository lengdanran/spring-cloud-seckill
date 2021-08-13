package com.danran.mq.service;

import com.danran.common.api.mq.SkMessage;
import com.danran.mq.config.MQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Classname MQProducer
 * @Description TODO
 * @Date 2021/8/8 18:50
 * @Created by ASUS
 */
@Service
public class MQProducer implements RabbitTemplate.ConfirmCallback {

    private static final Logger log = LoggerFactory.getLogger(MQProducer.class);

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // 设置 ack 回调
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendSkMessage(SkMessage message) {
        log.info("MQ send message: " + message);
        // 秒杀消息关联的数据
        CorrelationData skCorrData = new CorrelationData(UUID.randomUUID().toString());
        // 第一个参数为消息队列名(此处也为routingKey)，第二个参数为发送的消息
        rabbitTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, message, skCorrData);
    }

    /**
     * Confirmation callback.
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        assert correlationData != null;
        log.info("SkMessage UUID: " + correlationData.getId());
        if (ack) log.info("SkMessage 消息消费成功！");
        else log.info("SkMessage 消息消费失败！");
        if (cause != null) log.info("CallBackConfirm Cause: " + cause);
    }
}
