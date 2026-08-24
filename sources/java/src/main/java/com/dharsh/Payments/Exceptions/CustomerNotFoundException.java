package com.dharsh.Payments.Exceptions;

public class CustomerNotFoundException extends RuntimeException {
    
    public  CustomerNotFoundException(String message)
    {
       super(message);
    }
}
