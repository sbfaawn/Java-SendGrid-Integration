package com.digiserve.repository;

import com.digiserve.config.api.SendGridAPIConfiguration;
import com.digiserve.model.SendEmail;
import com.digiserve.model.SendGridResponse;
import com.digiserve.model.dto.EmailRequest;
import com.digiserve.model.dto.SendEmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EmailRepositoryImpl implements EmailRepository {
    ObjectMapper objectMapper = new ObjectMapper();
    private final SendGridAPIConfiguration sendGridAPIConfiguration;

    @Autowired
    public EmailRepositoryImpl(SendGridAPIConfiguration sendGridAPIConfiguration) {
        this.sendGridAPIConfiguration = sendGridAPIConfiguration;
    }

    @Override
    public SendGridResponse sendEmail(SendEmail sendEmail) {
        System.out.println(sendEmail);
        Email from = new Email(sendEmail.getEmailSender());

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(sendEmail.getTitle());

        if (sendEmail.getTemplateId() != null || isHtml(sendEmail.getContent())) {
            mail.addContent(new Content("text/html", sendEmail.getContent()));
        } else {
            mail.addContent(new Content("text/plain", sendEmail.getContent()));
        }

        Optional.ofNullable(sendEmail.getTemplateId())
                .ifPresent(templateId -> mail.setTemplateId(templateId));
//        mail.addAttachments(new Attachments());

        for (String emailReceiver : sendEmail.getEmailReceivers()) {
            Personalization personalization = new Personalization();
            personalization.addTo(new Email(emailReceiver));
            personalization.setSubject(sendEmail.getTitle());

            Optional.ofNullable(sendEmail.getSendAt())
                    .ifPresent(sendAt -> personalization.setSendAt(Long.getLong(sendAt)));
            Optional.ofNullable(sendEmail.getTemplateData())
                    .ifPresent(data -> data.forEach((key, value) ->
                            personalization.addDynamicTemplateData(key, value)
                    ));

            mail.addPersonalization(personalization);
        }
        // add attachment or any other setting in Mail class

        SendGrid sg = new SendGrid(sendGridAPIConfiguration.getApikey());
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
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

    private Boolean isHtml(String content) {
        return content.startsWith("<");
    }
}
