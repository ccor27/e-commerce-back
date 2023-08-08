package com.ccor.ecommerce.exceptions;

public class ProductSoldException extends RuntimeException{
    public ProductSoldException(String message) {
        super("PRODUCT_SOLD_EXCEPTION: "+message);
    }

    public ProductSoldException(String message, Throwable cause) {
        super(message, cause);
    }
}
