package com.dharsh.Payments.Model;

import java.util.*;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
@Entity
public class CustomerModel{
   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String phone;

    private Date CreatedAt;





}


