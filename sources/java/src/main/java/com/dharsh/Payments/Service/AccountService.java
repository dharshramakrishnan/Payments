package com.dharsh.Payments.Service;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dharsh.Payments.DTOs.CreateAccountRequests;
import com.dharsh.Payments.Exceptions.CustomerNotFoundException;
import com.dharsh.Payments.Model.AccountModel;
import com.dharsh.Payments.Model.AccountStatus;
import com.dharsh.Payments.Model.CustomerModel;
import com.dharsh.Payments.Repositories.AccountRepo;
import com.dharsh.Payments.Repositories.CustomerRepo;

@Service
public class AccountService {
    
    @Autowired
    private CustomerRepo customerRepo;
    
    @Autowired
    private AccountRepo accountRepo;


    public void createAccount(CreateAccountRequests request)
    {
        CustomerModel customer= customerRepo.findById(request.getCustomerId()).
        orElseThrow(()->
        new CustomerNotFoundException("Customer with id "+request.getCustomerId()+ "not found"));

        AccountModel account=new AccountModel();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(request.getCurrency());
        account.setStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);
        account.setCreatedAt(new Date());

        accountRepo.save(account);
        
    }

    public String generateAccountNumber()
    {
        String accountNumber = "ACCT" + System.currentTimeMillis();
        return accountNumber;
    }



}
