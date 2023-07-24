package com.digiserve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String title;
    private String content;
    private String sendTime;
    private String templateId;
    private Map<String, String> templateData;
}
