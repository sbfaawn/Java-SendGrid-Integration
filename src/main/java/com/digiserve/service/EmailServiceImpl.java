package com.digiserve.service;

import com.digiserve.model.SendEmail;
import com.digiserve.model.SendGridResponse;
import com.digiserve.model.dto.EmailRequest;
import com.digiserve.model.dto.SendEmailRequest;
import com.digiserve.repository.EmailRepository;
import com.digiserve.repository.SenderRepository;
import com.digiserve.thread.SendEmailControllerThread;
import com.digiserve.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private TaskExecutor taskExecutor;
    private ApplicationContext applicationContext;
    private EmailValidator emailValidator;

    @Autowired
    public EmailServiceImpl(TaskExecutor taskExecutor, ApplicationContext applicationContext, EmailValidator emailValidator) {
        this.taskExecutor = taskExecutor;
        this.applicationContext = applicationContext;
        this.emailValidator = emailValidator;
    }

    @Override
    public Map<String, String> sendEmail(SendEmailRequest request) {
        Map<String, String> map = new HashMap<>();

        if (!emailValidator.isValidEmail(request.getEmailSender())) {
            map.put("status", "400");
            map.put("message", "Failed");
            map.put("data", "Email for Sender \"" + request.getEmailSender() + "\" is not valid, No email is sent.");
            return map;
        }

        for (String emailReceiver : request.getEmailReceivers()) {
            if (!emailValidator.isValidEmail(emailReceiver)) {
                map.put("status", "400");
                map.put("message", "Failed");
                map.put("data", "Email for Receiver \"" + emailReceiver + "\" is not valid, No email is sent.");
                return map;
            }
        }

        for (EmailRequest email : request.getEmails()) {
            SendEmailControllerThread sendEmailControllerThread = applicationContext
                    .getBean(SendEmailControllerThread.class);
            SendEmail sendEmail = new SendEmail();
            sendEmail.setEmailSender(request.getEmailSender());
            sendEmail.setEmailReceivers(request.getEmailReceivers());
            sendEmail.setTitle(email.getTitle());
            sendEmail.setContent(email.getContent());
            sendEmail.setSendAt(email.getSendTime());
            sendEmail.setTemplateId(email.getTemplateId());
            sendEmail.setTemplateData(email.getTemplateData());
            sendEmailControllerThread.setSendEmail(sendEmail);
            taskExecutor.execute(sendEmailControllerThread);
        }
        map.put("status", "200");
        map.put("message", "Success");
        return map;
    }
}
