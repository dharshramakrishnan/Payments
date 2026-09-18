package com.dharsh.Payments.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dharsh.Payments.DTOs.AccountStatusOperation;
import com.dharsh.Payments.DTOs.AmountDeposit;
import com.dharsh.Payments.DTOs.AmountWithdrawal;
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
    
    @PostMapping ("/updateAccountStatus/{accountId}")
    public ResponseEntity<String> updateAccountStatus(@RequestBody  AccountStatusOperation request, @PathVariable Long accountId)
    {
        accountService.updateAccountStatus(request, accountId);
        return new ResponseEntity<>("Account status updated successfully",HttpStatus.OK);
    }
    
    @PostMapping("/depositAmount/{accountId}")
    public ResponseEntity<String> depositeAmount(@RequestBody AmountDeposit request, @PathVariable Long accountId)
    {
        accountService.depositAmount(request, accountId);
        return new ResponseEntity<>("Amount deposited Successfully",HttpStatus.OK);
    }

    @PostMapping("/withdrawAmount/{accountId}")
    public ResponseEntity<String> withdrawAmount(@RequestBody AmountWithdrawal request, @PathVariable Long accountId)
    {
        accountService.withdrawAmount(request,accountId);
        return new ResponseEntity<>("Amount debited Successfully",HttpStatus.OK);
    }

    
}
