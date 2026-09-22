package com.dharsh.Payments.Model;

import java.math.BigDecimal;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
public class AccountModel {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column (name="account_number")
    private String accountNumber;
    //private String CraccountNumber;
    private BigDecimal balance;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private CustomerModel customer;

    @Version 
    private Long version;
    
}
