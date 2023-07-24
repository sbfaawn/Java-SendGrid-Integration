package com.digiserve.thread;

import com.digiserve.model.SendEmail;
import com.digiserve.model.SendGridResponse;
import com.digiserve.repository.EmailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Component
@Scope(scopeName = "prototype")
public class SendEmailControllerThread implements Runnable {
    private Logger logger = LoggerFactory.getLogger(SendEmailControllerThread.class);

    @Setter
    @Getter
    private SendEmail sendEmail;

    private final EmailRepository emailRepository;

    @Autowired
    public SendEmailControllerThread(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public void run() {
        logger.info("SendEmailControllerThread - start thread");
        sendEmail();
    }

    private void sendEmail() {
        System.out.println(sendEmail);
        SendGridResponse sendGridResponse = emailRepository.sendEmail(sendEmail);
        System.out.println("Send Email Return With Status Code : " + sendGridResponse.getStatusCode());
    }


}
