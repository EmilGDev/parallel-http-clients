package com.example.parallelhttpclients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ParallelHttpClients1Application {

    public static void main(String[] args) {
        SpringApplication.run(ParallelHttpClients1Application.class, args);
    }

}
