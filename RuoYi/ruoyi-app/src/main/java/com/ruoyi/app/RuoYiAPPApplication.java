package com.ruoyi.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(scanBasePackages ="com.ruoyi"
,exclude = { DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
@MapperScan("com.ruoyi.*.mapper")
public class RuoYiAPPApplication {
    public static void main(String[] args){

        SpringApplication.run(RuoYiAPPApplication.class, args);
    }
}