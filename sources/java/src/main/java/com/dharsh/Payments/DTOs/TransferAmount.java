package com.dharsh.Payments.DTOs;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class TransferAmount {
    
    @NotNull 
    private String SenderAccountNumber;
    @NotNull 
    private String ReceiverAccountNumber;
    @NotNull 
    @DecimalMin (value = "0.01", message =" Transfer amount must be greather than 0" )
    private BigDecimal amount;
}
