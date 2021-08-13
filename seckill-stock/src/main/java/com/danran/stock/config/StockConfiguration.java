package com.danran.stock.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Classname StockConfiguration
 * @Description TODO
 * @Date 2021/8/2 14:19
 * @Created by ASUS
 */
@Configuration
public class StockConfiguration {

//    @Bean
//    public RestTemplateBuilder configRestTemplateBuilder() {
//        return new RestTemplateBuilder();
//    }

    @Bean
    @LoadBalanced
    public RestTemplate configRestTemplate(RestTemplateBuilder builder) {
        return builder.build(RestTemplate.class);
    }
}
