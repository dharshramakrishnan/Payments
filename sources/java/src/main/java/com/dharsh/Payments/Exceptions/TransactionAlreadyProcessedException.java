package com.dharsh.Payments.Exceptions;

public class TransactionAlreadyProcessedException extends RuntimeException {

    public TransactionAlreadyProcessedException(String message)
    {
        super(message);
    }
    
}
