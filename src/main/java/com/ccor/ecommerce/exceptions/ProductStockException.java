package com.ccor.ecommerce.exceptions;

public class ProductStockException extends RuntimeException{

    public ProductStockException(String message){
        super("PRODUCT_STOCk_EXCEPTION: "+message);
    }
    public ProductStockException(String message,Throwable cause){
        super(message,cause);
    }
}
