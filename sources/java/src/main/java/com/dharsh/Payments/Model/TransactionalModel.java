package com.dharsh.Payments.Model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data 
@Entity
public class TransactionalModel {
    
    @Id 
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private int id;

    private String TransactionID;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated (EnumType.STRING)
    private TransactionalStatus transactionalStatus;

    @ManyToOne 
    private AccountModel senderAccount;

    @ManyToOne
    private AccountModel receiverAccount;

}
