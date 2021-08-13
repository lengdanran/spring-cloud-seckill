package com.danran.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Classname StockController
 * @Description TODO
 * @Date 2021/8/2 14:17
 * @Created by ASUS
 */
@RestController
@RequestMapping(value = "/stock")
public class StockController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/create_order", method = RequestMethod.GET)
    public String invokeOrderCreateOrder() {
        return restTemplate.getForObject("http://order-service/order/create_order", String.class);
    }
}
