package com.poho.stuup.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wupeng
 */
//@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.poho.stuup")
@MapperScan("com.poho.stuup.dao")
public class ApiMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiMainApplication.class, args);
    }
}
