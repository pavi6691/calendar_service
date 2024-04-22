package com.acme.calendar.service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@OpenAPIDefinition(info = @Info(title = "Calendar Service", version = "0.0.0-SNAPSHOT", description = "Calendar Service REST APIs"))
@SpringBootApplication
@EnableTransactionManagement
public class CalendarApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalendarApplication.class, args);
    }
}
