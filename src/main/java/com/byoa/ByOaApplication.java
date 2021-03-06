package com.byoa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.byoa.*.dao")
@SpringBootApplication
@EnableCaching
public class ByOaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ByOaApplication.class, args);
        System.out.println("byoa启动成功");
        System.out.println("~~~~~~~~~~ysk~~~~~~~~~");
    }
}
