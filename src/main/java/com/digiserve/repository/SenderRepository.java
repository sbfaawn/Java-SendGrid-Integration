package com.digiserve.repository;

import com.digiserve.model.SendGridResponse;

import java.util.List;

public interface SenderRepository {
    public SendGridResponse getAuthenticatedSenders();

    public List<String> getAuthenticatedSenderById(String id);

    public SendGridResponse verificationRequest(String senderId);
}
