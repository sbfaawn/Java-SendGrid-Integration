package com.digiserve.controller;

import com.digiserve.config.api.SendGridAPIConfiguration;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/sendGrid")
public class ProjectController {
    private SendGridAPIConfiguration sendGridAPIConfiguration;

    @Autowired
    public ProjectController(SendGridAPIConfiguration sendGridAPIConfiguration) {
        this.sendGridAPIConfiguration = sendGridAPIConfiguration;
    }

    @RequestMapping(value = "/healthCheck", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Twilio SendGrip Project is Up");

        return response;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> test() {
        Email from = new Email("andhika.pratama@digiserve.co.id");
        String subject = "Sending with SendGrid is Fun";
        Email to1 = new Email("andhikarizki00000@gmail.com");
        Email to2 = new Email("andhikarizki00001@gmail.com");
        Content content1 = new Content("text/plain", "and easy to do anywhere, even with Java");
        Content content2 = new Content("text/plain", "good day my friend");
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content1);
        mail.addContent(content2);

        Personalization personalization1 = new Personalization();
        personalization1.addTo(to1);
        personalization1.addTo(to2);
        personalization1.setSubject("title 1");

        Personalization personalization2 = new Personalization();
        personalization2.addTo(to1);
        personalization2.addTo(to2);
        personalization2.setSubject("title 2");

        mail.addPersonalization(personalization2);

        SendGrid sg = new SendGrid(sendGridAPIConfiguration.getApikey());
        Request request = new Request();
        Response response = null;
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.getStackTrace();
        }

        Map<String, String> responseDto = new HashMap<>();
        responseDto.put("message", (response == null ? "Failed" : "Success"));

        return responseDto;
    }
}
