package com.ccor.ecommerce.service.registration;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImp implements IEmailSender{
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage,"utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("Store@gmail.com");
            javaMailSender.send(mimeMailMessage);
        }catch (MessagingException e){
            throw new IllegalStateException("Failed to send the email");
        }
    }
}
