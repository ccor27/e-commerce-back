package com.ccor.ecommerce.exceptions;

public class CustomerException extends RuntimeException{
    public CustomerException(String message) {
        super("CUSTOMER_EXCEPTION: "+message);
    }

    public CustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
