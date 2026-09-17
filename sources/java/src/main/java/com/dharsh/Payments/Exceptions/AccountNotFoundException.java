package com.dharsh.Payments.Exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message)
    {
        super(message);
    }
    
}
