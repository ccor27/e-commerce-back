package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ChannelNotification;
import com.ccor.ecommerce.model.Customer;
import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.repository.CustomerRepository;
import com.ccor.ecommerce.service.registration.IEmailSender;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {
    private CustomerRepository customerRepository;
    private IEmailSender iEmailSender;
    @Value("${notification.sms.account}")
    private String ACCOUNT_SID;
    @Value("${notification.sms.token}")
    private String AUTH_TOKEN;
    @Value("${notification.sms.phone}")
    private String TWILIO_NUMBER;
    @Autowired
    public NotificationServiceImpl(CustomerRepository customerRepository, IEmailSender iEmailSender) {
        this.customerRepository = customerRepository;
        this.iEmailSender = iEmailSender;
    }

    @Override
    @Transactional
    @Async
    public void notifyByEmailNewProduct(String message) {
        List<Customer> customers = customerRepository.findAllNotDeletedAndCanReceiveNotifications();
        if(customers!=null && !customers.isEmpty()){
            for (Customer c:customers ) {
                if(c.getChannelNotifications().contains(ChannelNotification.EMAIL)){
                    String messageToSend = buildEmailNotification(c.getUsername(),message);
                    iEmailSender.sendEmail(c.getEmail(),messageToSend,"New product available!");
                }
            }
        }
    }

    @Override
    @Transactional
    @Async
    public void notifyBySmsNewProduct(String message) {
        List<Customer> customers = customerRepository.findAllNotDeletedAndCanReceiveNotifications();
        if(customers!=null && !customers.isEmpty()){
            for (Customer c:customers ) {
                if(c.getChannelNotifications().contains(ChannelNotification.SMS)){
                    /*Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                     Message.creator(
                                    new com.twilio.type.PhoneNumber(c.getCellphone()),
                                    new PhoneNumber(TWILIO_NUMBER),
                                    message)
                            .create();*/
                    System.out.println("SMS: new product");

                }
            }
        }
    }

    @Override
    @Async
    public void notifyByEmailWelcomeNewUser(String email,String username, String message) {
        String messageToSend = buildEmailNotification(username,message);
        iEmailSender.sendEmail(email,messageToSend,"Welcome to our ecommerce");
    }

    @Override
    @Async
    public void notifyBySmsWelcomeNewUser(String cellphone, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                        new com.twilio.type.PhoneNumber(cellphone),
                        new PhoneNumber(TWILIO_NUMBER),
                        message)
                .create();
        System.out.println("SMS: welcome new user");
    }

    @Override
    @Transactional
    @Async
    public void notifyByEmailNewSale(Long customerId, String message) {
     Customer c = customerRepository.findById(customerId).orElse(null);
     if(c!=null){
         String messageToSend = buildEmailNotification(c.getUsername(),message);
         iEmailSender.sendEmail(c.getEmail(),messageToSend,"Order created from ecommerce");
     }
    }

    @Override
    @Transactional
    @Async
    public void notifyBySmsNewSale(Long customerId, String message) {
        Customer c = customerRepository.findById(customerId).orElse(null);
        if(c!=null){
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message.creator(
                            new com.twilio.type.PhoneNumber(c.getCellphone()),
                            new PhoneNumber(TWILIO_NUMBER),
                            message)
                    .create();
            System.out.println("SMS: new sale");
        }
    }

    @Override
    @Transactional
    @Async
    public void notifyByEmailConfirmedPayment(Long customerId, String message) {
     Customer c = customerRepository.findById(customerId).orElse(null);
     if(c!=null){
         String messageToSend = buildEmailNotification(c.getUsername(),message);
         iEmailSender.sendEmail(c.getEmail(),messageToSend,"Order confirmation from ecommerce");
     }
    }

    @Override
    @Transactional
    @Async
    public void notifyBySmsConfirmedPayment(Long customerId, String message) {
        Customer c = customerRepository.findById(customerId).orElse(null);
        if(c!=null){
            /*Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message.creator(
                            new com.twilio.type.PhoneNumber(c.getCellphone()),
                            new PhoneNumber(TWILIO_NUMBER),
                            message)
                    .create();*/
            System.out.println("SMS: payment confirmed");
        }
    }

    @Override
    @Transactional
    @Async
    public void notifyByEmailPaymentCancelled(Long customerId, String message) {
        Customer c = customerRepository.findById(customerId).orElse(null);
        if(c!=null){
            String messageToSend = buildEmailNotification(c.getUsername(),message);
            iEmailSender.sendEmail(c.getEmail(),messageToSend,"Order cancelled from ecommerce");
        }
    }

    @Override
    @Transactional
    @Async
    public void notifyBySmsPaymentCancelled(Long customerId, String message) {
        Customer c = customerRepository.findById(customerId).orElse(null);
        if(c!=null){
            /*Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message.creator(
                            new com.twilio.type.PhoneNumber(c.getCellphone()),
                            new PhoneNumber(TWILIO_NUMBER),
                            message)
                    .create();*/
            System.out.println("SMS: payment cancelled");
        }
    }

    private String buildEmailNotification(String name, String message) {
        return "<div style=\"font-family:Helvetica, Arial, sans-serif; font-size:16px; margin:0; color:#0b0c0c\">\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">Hi " + name + ",</p>\n" +
                "    <p style=\"Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c\">" + message + "</p>\n" +
                "</div>";
    }

}
