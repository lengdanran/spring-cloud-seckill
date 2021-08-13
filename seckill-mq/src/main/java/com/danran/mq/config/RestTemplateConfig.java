package com.danran.mq.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Classname RestTemplateConfig
 * @Description TODO
 * @Date 2021/8/8 14:12
 * @Created by ASUS
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate configRestTemplate(RestTemplateBuilder builder) {
        return builder.build(RestTemplate.class);
    }
}
