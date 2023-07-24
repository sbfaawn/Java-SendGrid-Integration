package com.digiserve.repository;

import com.digiserve.config.api.SendGridAPIConfiguration;
import com.digiserve.model.SendGridResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SenderRepositoryImpl implements SenderRepository {
    ObjectMapper objectMapper = new ObjectMapper();

    private SendGridAPIConfiguration sendGridAPIConfiguration;

    @Autowired
    public SenderRepositoryImpl(SendGridAPIConfiguration sendGridAPIConfiguration) {
        this.sendGridAPIConfiguration = sendGridAPIConfiguration;
    }

    @Override
    public SendGridResponse getAuthenticatedSenders() {
        Response response = null;
        try {
            SendGrid sg = new SendGrid(sendGridAPIConfiguration.getApikey());
            Request request = new Request();
            request.setMethod(Method.GET);
            request.setEndpoint("/verified_senders");
            response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

            return generateSendGridResponse(response);
        } catch (IOException ex) {
            ex.getStackTrace();
            return generateErrorResponse(ex.getMessage());
        }
    }

    @Override
    public List<String> getAuthenticatedSenderById(String id) {
        return null;
    }

    @Override
    public SendGridResponse verificationRequest(String senderId) {
        try {
            SendGrid sg = new SendGrid(sendGridAPIConfiguration.getApikey());
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("/verified_senders/resend/" + senderId);
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

            return generateSendGridResponse(response);
        } catch (IOException ex) {
            ex.getStackTrace();
            return generateErrorResponse(ex.getMessage());
        }
    }

    private SendGridResponse generateSendGridResponse(Response response) throws JsonProcessingException {
        Map<String, String> map = objectMapper.readValue(response.getBody(), HashMap.class);
        return new SendGridResponse(response.getStatusCode(), map.get("results"), response.getHeaders());
    }

    private SendGridResponse generateErrorResponse(String message) {
        return new SendGridResponse(400, message, new HashMap<>());
    }
}
