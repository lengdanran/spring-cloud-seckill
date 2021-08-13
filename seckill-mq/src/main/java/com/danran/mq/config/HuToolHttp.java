package com.danran.mq.config;

import cn.hutool.http.HttpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname HuToolHttp
 * @Description TODO
 * @Date 2021/8/8 15:00
 * @Created by ASUS
 */
@Configuration
public class HuToolHttp {
    @Bean
    public HttpUtil httpUtil() {
        return new HttpUtil();
    }
}
