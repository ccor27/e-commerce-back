package com.ccor.ecommerce.exceptions;

public class CancelSaleException extends RuntimeException{
    public CancelSaleException(String message) {
        super("CANCEL_SALE_EXCEPTION: "+message);
    }

    public CancelSaleException(String message, Throwable cause) {
        super(message, cause);
    }
}
