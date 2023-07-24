package com.digiserve.controller;

import com.digiserve.config.api.SendGridAPIConfiguration;
import com.digiserve.model.dto.APIResponse;
import com.digiserve.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/sendGrid")
public class TemplateController {
    private SendGridAPIConfiguration sendGridAPIConfiguration;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public TemplateController(SendGridAPIConfiguration sendGridAPIConfiguration) {
        this.sendGridAPIConfiguration = sendGridAPIConfiguration;
    }

    @RequestMapping(value = "/templates", method = RequestMethod.GET)
    public @ResponseBody APIResponse getTemplates() throws JsonProcessingException {
        Response sgResponse = null;
        try {
            SendGrid sg = new SendGrid(sendGridAPIConfiguration.getApikey());
            Request request = new Request();
            request.setMethod(Method.GET);
            request.setEndpoint("/templates");
            request.addQueryParam("generations", "dynamic");
            request.addQueryParam("page_size", "18");
            sgResponse = sg.api(request);
            System.out.println(sgResponse.getStatusCode());
            System.out.println(sgResponse.getBody());
            System.out.println(sgResponse.getHeaders());
        } catch (IOException ex) {
            ex.getStackTrace();
        }

        System.out.println("END");
        APIResponse response = new APIResponse();
        response.setStatusCode("200");
        response.setMessage(sgResponse == null ? "Failed" : "Success");
        response.setData(sgResponse == null ? "" : objectMapper.readValue(sgResponse.getBody(), HashMap.class).get("result"));

        return response;
    }

    private APIResponse generateAPIResponse(Map<String, String> serviceResult) {
        APIResponse response = new APIResponse();
        response.setStatusCode(serviceResult.get("status"));
        response.setData(serviceResult.get("data"));
        response.setMessage(serviceResult.get("message"));

        return response;
    }
}