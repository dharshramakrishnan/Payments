package com.dharsh.Payments.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dharsh.Payments.DTOs.CreateAccountRequests;
import com.dharsh.Payments.Service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")

public class AccountController {


    @Autowired
    private AccountService accountService;

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@Valid @RequestBody CreateAccountRequests request){

        accountService.createAccount(request);
        return new ResponseEntity<>("Account Created Successfully",HttpStatus.CREATED);
    }
    
}
