package com.yang.auto;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication()
@MapperScan(basePackages = "com.yang.auto.mapper")
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.yang"})
public class AutoClockApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoClockApplication.class, args);
    }

}
