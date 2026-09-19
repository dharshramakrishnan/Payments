package com.dharsh.Payments.Model;

import java.security.Identity;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Entity 
@Data 
public class Idempotency {

    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int Id;

    @Column(unique=true, nullable = false)
    private String idempotencyKey;

    private String TransactionId;
    private Date CreatedAt;
    
}
