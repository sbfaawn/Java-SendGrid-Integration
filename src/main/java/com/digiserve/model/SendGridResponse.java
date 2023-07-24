package com.digiserve.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendGridResponse {
    private Integer statusCode;
    private String results;
    private Map<String, String> headers;
}
