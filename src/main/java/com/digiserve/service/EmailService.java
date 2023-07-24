package com.digiserve.service;

import com.digiserve.model.dto.SendEmailRequest;

import java.util.List;
import java.util.Map;

public interface EmailService {
    public Map<String, String> sendEmail(SendEmailRequest request);
}
