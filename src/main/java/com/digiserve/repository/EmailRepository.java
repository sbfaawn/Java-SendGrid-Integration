package com.digiserve.repository;

import com.digiserve.model.SendEmail;
import com.digiserve.model.SendGridResponse;
import com.digiserve.model.dto.SendEmailRequest;

import java.util.List;

public interface EmailRepository {
    public SendGridResponse sendEmail(SendEmail sendEmail);

}
