package com.coffeester.ticketing;

import com.mangofactory.swagger.plugin.EnableSwagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@EnableSwagger
public class TicketingServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingServicesApplication.class, args);
    }
}
