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
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
@Entity
public class TransactionalModel {
    
    @Id 
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "transactionid")
    private String transactionId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated (EnumType.STRING)
    private TransactionalStatus transactionalStatus;

    @ManyToOne 
    private AccountModel senderAccount;

    @ManyToOne
    private AccountModel receiverAccount;
    
    @NotNull 
    private Date createdAt;

    private String SenderAccountNumber;
    private String ReceiverAccountNumber;

}
