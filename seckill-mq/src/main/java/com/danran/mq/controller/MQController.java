package com.danran.mq.controller;

import com.alibaba.fastjson.JSON;
import com.danran.common.api.mq.SkMessage;
import com.danran.mq.service.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname MQController
 * @Description TODO
 * @Date 2021/8/8 15:13
 * @Created by ASUS
 */
@RestController
@RequestMapping("/mq")
public class MQController {

    @Autowired
    private MQProducer mqProducer;

    @GetMapping("/test")
    public String test(@RequestParam("user_id") int userId,
                       @RequestParam("book_id") String bookId) {
        System.out.println(userId);
        System.out.println(bookId);
        return "200";
    }

    @PostMapping("/send_message")
    public void sendMessage(@RequestParam("message") String message) {
        System.out.println(message);
        SkMessage msg = JSON.parseObject(message, SkMessage.class);
        mqProducer.sendSkMessage(msg);
    }
}
