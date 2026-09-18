package com.dharsh.Payments.DTOs;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class AmountWithdrawal {

    @NotNull 
    @DecimalMax (value="15000.00" , message="Amount must be less than or equal to 15000")
    private BigDecimal amount;

    @NotNull 
    private String ReceiverAccountNumber;
    
    @NotNull 
    private String SenderAccountNumber;
    
}
