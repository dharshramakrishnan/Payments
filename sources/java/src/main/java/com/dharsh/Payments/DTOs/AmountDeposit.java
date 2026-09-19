package com.dharsh.Payments.DTOs;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AmountDeposit {
    
    @NotNull
    @DecimalMin (value="0.01", message="Amount must be greater than 0")
    private BigDecimal amount;

}
