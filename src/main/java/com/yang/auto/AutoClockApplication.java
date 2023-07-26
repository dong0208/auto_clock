package com.yang.auto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.yang"})
public class AutoClockApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoClockApplication.class, args);
    }

}
