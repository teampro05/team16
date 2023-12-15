package com.pro06.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class DataAudingConfig {
    //Entity > BaseEntity에 localdatetime 시간 적용
    public static void main(String[] args) {
        SpringApplication.run(DataAudingConfig.class, args);
    }
}
