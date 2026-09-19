package com.dharsh.Payments.Exceptions;


public class InsufficientBalanceException extends RuntimeException{
    
    public InsufficientBalanceException(String message)
    {
         super(message);
    }
}
