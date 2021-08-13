package com.danran.gateway.config;

import com.danran.common.service.URLService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Classname BeanConfig
 * @Description TODO
 * @Date 2021/8/9 14:13
 * @Created by ASUS
 */
@Configuration
public class BeanConfig {
    @Bean
    public URLService urlService() {
        return new URLService();
    }

}
