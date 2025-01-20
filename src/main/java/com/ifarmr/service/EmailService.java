package com.ifarmr.service;

import com.ifarmr.payload.response.EmailDetails;

public interface EmailService {

    void sendEmailToken(EmailDetails emailDetails);

    void forgetPasswordAlert(EmailDetails emailDetails);

    void sendEmailAlert(EmailDetails emailDetails);

    void forgetPasswordUpdateAlert(EmailDetails emailDetails);
}