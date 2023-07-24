package com.digiserve.controller;

import com.digiserve.model.dto.EmailRequest;
import com.digiserve.model.dto.SendEmailRequest;
import com.digiserve.model.dto.APIResponse;
import com.digiserve.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/api/sendGrid")
public class EmailController {
    private EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public @ResponseBody APIResponse sendEmail(@RequestBody SendEmailRequest emailRequest) {
        Map<String, String> map = emailService.sendEmail(emailRequest);

        APIResponse sendEmailResponse = generateAPIResponse(map);
        System.out.println("END");
        return sendEmailResponse;
    }

    private APIResponse generateAPIResponse(Map<String, String> serviceResult) {
        APIResponse response = new APIResponse();
        response.setStatusCode(serviceResult.get("status"));
        response.setData(serviceResult.get("data"));
        response.setMessage(serviceResult.get("message"));

        return response;
    }
}
