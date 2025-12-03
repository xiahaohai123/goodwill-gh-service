package com.wangkang.wangkangdataetlservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WangkangDataEtlServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WangkangDataEtlServiceApplication.class, args);
    }

}
