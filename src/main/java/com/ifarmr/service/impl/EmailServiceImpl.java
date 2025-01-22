package com.ifarmr.service.impl;

import com.ifarmr.payload.response.EmailDetails;
import com.ifarmr.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailToken(EmailDetails emailDetails) {


        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessageBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(simpleMailMessage);
            System.out.println("Token Email sent successfully!");
        } catch (MailException e) {
            throw new RuntimeException("Token Email not sent");
        }
    }

    @Override
    public void forgetPasswordAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessageBody());
            System.out.println("Email Service: email body and receipient added");
            simpleMailMessage.setSubject(emailDetails.getSubject());
            System.out.println("Email details prepared for sending");

            javaMailSender.send(simpleMailMessage);
            System.out.println("Token Email sent successfully!");
        } catch (MailException e) {
            throw new RuntimeException("Token Email not sent");
        }

    }

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessageBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(simpleMailMessage);
            System.out.println("Mail sent successfully!");
        } catch (MailException e) {
            throw new RuntimeException("Email not sent");
        }
    }

    @Override
    public void forgetPasswordUpdateAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMessageBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(simpleMailMessage);
            System.out.println("Password Reset Successful Notification Email sent successfully!");
        } catch (MailException e) {
            throw new RuntimeException("Password Reset Successful Notification Email not sent");
        }
    }
}
