package com.yang.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.yang.server.mapper")
@EnableTransactionManagement
@ComponentScan("com.yang")
public class AutoClockApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoClockApplication.class, args);
    }

}
