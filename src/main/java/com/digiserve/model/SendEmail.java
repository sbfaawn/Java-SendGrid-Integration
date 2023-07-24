package com.digiserve.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmail {
    private String emailSender;
    private List<String> emailReceivers;
    private String title;
    private String content;
    private String sendAt;
    private String templateId;
    private Map<String, String> templateData;
}
