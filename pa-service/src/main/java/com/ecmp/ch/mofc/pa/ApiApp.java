package com.ecmp.ch.mofc.pa;

import com.ecmp.spring.boot.autoconfigure.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * description:启动类
 * author:august
 * date:8/20/2018
 */
@EntityScan("com.ecmp.ch.mofc")
@ComponentScan("com.ecmp.ch.mofc")
@SpringBootApplication(exclude = {WebConfig.class})
public class ApiApp {

    public static void main(String[] args) {
        SpringApplication.run(ApiApp.class, args);
    }
}
