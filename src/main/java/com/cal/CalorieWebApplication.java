package com.cal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

// 1. We keep your database exclusions so the app doesn't crash on startup
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class, 
    R2dbcAutoConfiguration.class
})
// 2. We keep the Scheduling enabled so your calories reset at midnight
@EnableScheduling
public class CalorieWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalorieWebApplication.class, args);
    }
}