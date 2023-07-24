package com.digiserve.controller;

import com.digiserve.config.api.SendGridAPIConfiguration;
import com.digiserve.model.dto.APIResponse;
import com.digiserve.service.EmailService;
import com.digiserve.service.SenderService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/sendGrid")
public class SenderController {
    private final SenderService senderService;
    private SendGridAPIConfiguration sendGridAPIConfiguration;

    @Autowired
    public SenderController(SenderService senderService, SendGridAPIConfiguration sendGridAPIConfiguration) {
        this.senderService = senderService;
        this.sendGridAPIConfiguration = sendGridAPIConfiguration;
    }

    @RequestMapping(value = "/authenticatedSenders", method = RequestMethod.GET)
    public @ResponseBody APIResponse authenticatedSender() {
        Map<String, String> map = senderService.getAuthenticatedSender();
        APIResponse response = generateAPIResponse(map);

        System.out.println("--- END ---");
        return response;
    }

    @RequestMapping(value = "/completedSteps", method = RequestMethod.GET)
    public @ResponseBody APIResponse completedSteps() {
        // refactor later
        try {
            SendGrid sg = new SendGrid(sendGridAPIConfiguration.getApikey());
            Request request = new Request();
            request.setMethod(Method.GET);
            request.setEndpoint("/verified_senders/steps_completed");
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.getStackTrace();
        }

        System.out.println("END");
        APIResponse response = new APIResponse();
        response.setStatusCode("200");
        response.setMessage("");

        return response;
    }

    @RequestMapping(value = "/createSender", method = RequestMethod.GET)
    public @ResponseBody APIResponse createSender() {
        System.out.println("1");
        // refactor later
        try {
            SendGrid sg = new SendGrid(sendGridAPIConfiguration.getApikey());
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("/verified_senders");
            request.setBody("{\n" +
                    "  \"nickname\": \"Orders\",\n" +
                    "  \"from_email\": \"andhikarizki00000@gmail.com\",\n" +
                    "  \"from_name\": \"Marilah Seluruh Rkayat Filipina\",\n" +
                    "  \"reply_to\": \"andhikarizki00000@gmail.com\",\n" +
                    "  \"reply_to_name\": \"Marilah Seluruh Rkayat Filipina\",\n" +
                    "  \"address\": \"Jalan Kenangan\",\n" +
                    "  \"city\": \"Jakarta\",\n" +
                    "  \"country\": \"Indonesia\",\n" +
                    "  \"zip\": \"12312\"\n" +
                    "}");
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.getStackTrace();
        }

        System.out.println("END");
        APIResponse response = new APIResponse();
        response.setStatusCode("200");
        response.setMessage("");

        return response;
    }

    @RequestMapping(value = "/verificationRequest", method = RequestMethod.GET)
    public @ResponseBody APIResponse verificationRequest(@RequestParam String senderId) {
        Map<String, String> map = senderService.verificationEmailRequest(senderId);
        APIResponse response = generateAPIResponse(map);

        System.out.println("--- END ---");
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