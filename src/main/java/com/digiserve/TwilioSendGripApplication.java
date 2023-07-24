package com.digiserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class TwilioSendGripApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwilioSendGripApplication.class, args);
    }

}
