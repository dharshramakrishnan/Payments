package com.dharsh.Payments.DTOs;

import com.dharsh.Payments.Model.AccountStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class AccountStatusOperation {
    
    @NotNull(message = "Account status cannot be null") 
    private AccountStatus status;

}
