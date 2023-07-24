package com.digiserve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {
    private String emailSender;
    private List<String> emailReceivers;
    private List<EmailRequest> emails;
}
