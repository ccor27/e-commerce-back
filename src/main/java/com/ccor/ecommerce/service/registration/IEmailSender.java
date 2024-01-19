package com.ccor.ecommerce.service.registration;

public interface IEmailSender {
    void sendEmail(String to, String email, String title);


}
