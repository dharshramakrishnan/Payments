package com.dharsh.Payments.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequests {
    
    @NotNull
    private Long customerId;

    @NotNull
    private String currency;
}
