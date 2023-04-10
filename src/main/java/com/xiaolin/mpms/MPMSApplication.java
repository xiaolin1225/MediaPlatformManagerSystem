package com.xiaolin.mpms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xiaolin.mpms.mapper")
public class MPMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(MPMSApplication.class, args);
    }

}
