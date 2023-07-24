package com.digiserve.service;

import com.digiserve.model.SendGridResponse;
import com.digiserve.repository.SenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SenderServiceImpl implements SenderService {
    private SenderRepository senderRepository;

    @Autowired
    public SenderServiceImpl(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }

    @Override
    public Map<String, String> getAuthenticatedSender() {
        SendGridResponse sendGridResponse = senderRepository.getAuthenticatedSenders();
        return serviceResponse(sendGridResponse);
    }

    @Override
    public Map<String, String> verificationEmailRequest(String senderId) {
        SendGridResponse sendGridResponse = senderRepository.verificationRequest(senderId);
        return serviceResponse(sendGridResponse);
    }

    private Map<String, String> serviceResponse(SendGridResponse sendGridResponse) {
        Integer statusCode = sendGridResponse.getStatusCode();

        Map<String, String> response = new HashMap<>();
        response.put("statusCode", statusCode.toString());
        response.put("message", statusCode == 200 ? "Success" : "Failed");
        response.put("data", sendGridResponse.getResults());

        return response;
    }
}
