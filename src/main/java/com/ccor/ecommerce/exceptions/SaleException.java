package com.ccor.ecommerce.exceptions;

public class SaleException extends RuntimeException{
    public SaleException(String message) {
        super("SALE_EXCEPTION: "+message);
    }

    public SaleException(String message, Throwable cause) {
        super(message, cause);
    }
}
