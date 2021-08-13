package com.danran.book.config;

import com.danran.common.service.URLService;
import com.danran.common.util.OrderIDUtil;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname BeansConfig
 * @Description TODO
 * @Date 2021/8/8 20:55
 * @Created by ASUS
 */
@Configuration
public class BeansConfig {

    @Bean
    public OrderIDUtil orderIDUtil() {
        return new OrderIDUtil();
    }

    @Bean
    @LoadBalanced
    public URLService urlService() {
        return new URLService();
    }
}
