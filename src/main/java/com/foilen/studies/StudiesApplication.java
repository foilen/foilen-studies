package com.foilen.studies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StudiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudiesApplication.class, args);
    }

}
