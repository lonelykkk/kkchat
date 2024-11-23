package com.kkk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.kkk"})
//@MapperScan(basePackages = {"mappers"})
@EnableTransactionManagement
@EnableScheduling
public class kkkApplication {
    public static void main(String[] args) { 
        SpringApplication.run(kkkApplication.class, args);
    }
}
