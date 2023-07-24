package com.digiserve.service;

import com.digiserve.model.dto.APIResponse;

import java.util.List;
import java.util.Map;

public interface SenderService {
    public Map<String, String> getAuthenticatedSender();

    public Map<String, String> verificationEmailRequest(String senderId);
}
