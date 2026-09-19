package com.dharsh.Payments.DTOs;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data 
public class TransactionHistory {

    private int id;
    private String TransactionID;
    private String TransactionType;
    private String TransactionStatus;
    private String SenderAccountNumber;
    private String ReceiverAccountNumber;
    private Date TransactionTime;
    private BigDecimal amount;

    
}
