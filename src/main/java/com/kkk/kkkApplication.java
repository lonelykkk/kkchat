package com.kkk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.kkk"})
@MapperScan(basePackages = {"com.kkk.mappers"})
public class kkkApplication {
    public static void main(String[] args) {
        SpringApplication.run(kkkApplication.class, args);
    }
}