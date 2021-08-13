package com.danran.mq.config;

import com.danran.common.service.URLService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname BeasConfig
 * @Description TODO
 * @Date 2021/8/9 14:14
 * @Created by ASUS
 */
@Configuration
public class BeasConfig {
    @Bean
    @LoadBalanced
    public URLService urlService() {
        return new URLService();
    }
}
