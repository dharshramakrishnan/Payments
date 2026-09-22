package com.dharsh.Payments.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dharsh.Payments.DTOs.TransactionHistory;
import com.dharsh.Payments.DTOs.TransferAmount;
import com.dharsh.Payments.Service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping ("/transactions")
public class TransactionController {

    @Autowired 
    private TransactionService transactionService;
    
    @GetMapping ("/getAllTransactions")
    public List<TransactionHistory> getAllTransactions(){

         return transactionService.getAllTransactions();
    }
    
    @GetMapping ("/getAllTransactionsByAccountId/{accountId}")
    public List<TransactionHistory> getAllTransactionsByAccountId(@Valid @PathVariable Long accountId){
        return transactionService.getAllTransactionsByID(accountId);
    }

    @PostMapping("/TranferAmount")
    public ResponseEntity<TransactionHistory> transferAmount(@RequestHeader("Idempotency-key") String IdempotencyKey ,@Valid @RequestBody TransferAmount amount)
    {
        
        return new ResponseEntity<>(transactionService.transferAmount(IdempotencyKey,amount),HttpStatus.OK);
    }
    
}
