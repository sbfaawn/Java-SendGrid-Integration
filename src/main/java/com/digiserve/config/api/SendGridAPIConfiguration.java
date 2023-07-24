package com.digiserve.config.api;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "sendgrid")
public class SendGridAPIConfiguration {
    private String apikey;
}
