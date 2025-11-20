package com.zealepsoluciones.libertybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LibertybackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibertybackendApplication.class, args);
    }

}
