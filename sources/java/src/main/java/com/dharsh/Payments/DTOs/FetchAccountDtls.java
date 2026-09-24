package com.dharsh.Payments.DTOs;



import lombok.Data;

@Data 
public class FetchAccountDtls {

    private String AccountNumber;
    private String Balance;
    private String AccountStatus;
    private String currency;
    private String id;
    
}
