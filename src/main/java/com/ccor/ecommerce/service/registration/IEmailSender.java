package com.ccor.ecommerce.service.registration;

public interface IEmailSender {
    void send(String to, String email);
}
