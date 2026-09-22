package com.dharsh.Payments.DTOs;

import java.math.BigDecimal;

import com.dharsh.Payments.Model.AccountStatus;

import lombok.Data;

@Data 
public class CacheAccount {

    private Long id;
    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private AccountStatus status;
    
}
