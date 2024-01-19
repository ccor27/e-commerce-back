package com.ccor.ecommerce.service;

import com.ccor.ecommerce.model.ProductStock;

public interface INotificationService {

    void notifyByEmailNewProduct(String message);
    void notifyBySmsNewProduct( String message);
    void notifyByEmailWelcomeNewUser(String email,String username,String message);
    void notifyBySmsWelcomeNewUser(String cellphone,String message);
    void notifyByEmailNewSale(Long customerId, String message);
    void notifyBySmsNewSale(Long customerId, String message);
    void notifyByEmailConfirmedPayment(Long customerId, String message);
    void notifyBySmsConfirmedPayment(Long customerId, String message);
    void notifyByEmailPaymentCancelled(Long customerId, String message);
    void notifyBySmsPaymentCancelled(Long customerId, String message);


}
