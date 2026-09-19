package com.dharsh.Payments.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dharsh.Payments.DTOs.TransactionHistory;
import com.dharsh.Payments.DTOs.TransferAmount;
import com.dharsh.Payments.Exceptions.AccountNotFoundException;
import com.dharsh.Payments.Exceptions.InsufficientBalanceException;
import com.dharsh.Payments.Exceptions.TransactionAlreadyProcessedException;
import com.dharsh.Payments.Model.AccountModel;
import com.dharsh.Payments.Model.AccountStatus;
import com.dharsh.Payments.Model.Idempotency;
import com.dharsh.Payments.Model.TransactionType;
import com.dharsh.Payments.Model.TransactionalModel;
import com.dharsh.Payments.Model.TransactionalStatus;
import com.dharsh.Payments.Repositories.AccountRepo;
import com.dharsh.Payments.Repositories.IdempotencyRepo;
import com.dharsh.Payments.Repositories.TransactionRepo;

@Service 
public class TransactionService {
    
    @Autowired 
    private TransactionRepo transactionRepo;

    @Autowired 
    private AccountRepo accountRepo;

    @Autowired 
    private IdempotencyRepo idempotencyRepo;


    public List<TransactionHistory> getAllTransactions(){
         List<TransactionHistory> transaction =new ArrayList<>();
        

        for(TransactionalModel txn :transactionRepo.findAll()){
 
                TransactionHistory txn_hstry=new TransactionHistory();
                 txn_hstry.setId(txn.getId());
                 txn_hstry.setTransactionID(txn.getTransactionID());
                 txn_hstry.setTransactionStatus(txn.getTransactionalStatus().toString());
                 txn_hstry.setTransactionType(txn.getTransactionType().toString());
                 txn_hstry.setReceiverAccountNumber(txn.getReceiverAccountNumber());
                 txn_hstry.setSenderAccountNumber(txn.getSenderAccountNumber());
                 txn_hstry.setTransactionTime(txn.getCreatedAt());
                 txn_hstry.setAmount(txn.getAmount());

                 transaction.add(txn_hstry);
            }
            return transaction;
    }

    public List<TransactionHistory> getAllTransactionsByID(Long accountId){
        
        List<TransactionHistory> transaction =new ArrayList<>();
        

        for(TransactionalModel txn :transactionRepo.findAll()){
            TransactionHistory txn_hstry=new TransactionHistory();
            if(txn.getReceiverAccount() !=null && txn.getReceiverAccount().getId().equals(accountId))
            {
                 txn_hstry.setId(txn.getId());
                 txn_hstry.setTransactionID(txn.getTransactionID());
                 txn_hstry.setTransactionStatus(txn.getTransactionalStatus().toString());
                 txn_hstry.setTransactionType(txn.getTransactionType().toString());
                 txn_hstry.setReceiverAccountNumber(txn.getReceiverAccountNumber());
                 txn_hstry.setTransactionTime(txn.getCreatedAt());
                 txn_hstry.setAmount(txn.getAmount());
                 transaction.add(txn_hstry);    
                 
            }
            if(txn.getSenderAccount() !=null && txn.getSenderAccount().getId().equals(accountId))
            {
                 txn_hstry.setId(txn.getId());
                 txn_hstry.setTransactionID(txn.getTransactionID());
                 txn_hstry.setTransactionStatus(txn.getTransactionalStatus().toString());
                 txn_hstry.setTransactionType(txn.getTransactionType().toString());
                 txn_hstry.setSenderAccountNumber(txn.getSenderAccountNumber());
                 txn_hstry.setTransactionTime(txn.getCreatedAt());
                 txn_hstry.setAmount(txn.getAmount());
                transaction.add(txn_hstry);    
                
            }
           
            
        }

        return transaction;
    }
   
    @Transactional
    public void transferAmount(String IdempotencyKey, TransferAmount request)
    {
        Idempotency idempotency=idempotencyRepo.findByIdempotencyKey(IdempotencyKey).get();
        //.orElseThrow(()-> new TransactionAlreadyProcessedException("The transaction is processed already"));
        if(idempotency.getIdempotencyKey()==null)
        {
        Idempotency idmptncy=new Idempotency();
        AccountModel debit_acnt_dtls=accountRepo.findByAccountNumber(request.getSenderAccountNumber()).orElseThrow( ()-> new AccountNotFoundException("Account" +request.getSenderAccountNumber()+"does not exists"));
        AccountModel credit_acnt_dtls=accountRepo.findByAccountNumber(request.getReceiverAccountNumber()).orElseThrow(()-> new AccountNotFoundException("Account" + request.getReceiverAccountNumber()+"does not exists"));
        
        if(debit_acnt_dtls.getStatus()==AccountStatus.BLOCKED || debit_acnt_dtls.getStatus()==AccountStatus.CLOSED)
        {
            throw new IllegalStateException(" Debit Account is blocked or closed");
        }
         if(credit_acnt_dtls.getStatus()==AccountStatus.BLOCKED || credit_acnt_dtls.getStatus()==AccountStatus.CLOSED)
        {
            throw new IllegalStateException("Credit Account is blocked or closed");
        }

        if(request.getSenderAccountNumber().equalsIgnoreCase(request.getReceiverAccountNumber()))
        {
            throw new IllegalStateException("Sender and Receiver account cannot be same");
        }

        if(request.getAmount().compareTo(debit_acnt_dtls.getBalance())>0)
        {
            throw new InsufficientBalanceException("Payment failed due to insufficient balace in the account");
        }

        

        debit_acnt_dtls.setBalance(debit_acnt_dtls.getBalance().subtract(request.getAmount()));
        credit_acnt_dtls.setBalance(credit_acnt_dtls.getBalance().add(request.getAmount()));
        
        //creating the movement
        TransactionalModel movement=new TransactionalModel();
        movement.setTransactionID(UUID.randomUUID().toString());
        movement.setReceiverAccount(credit_acnt_dtls);
        movement.setSenderAccount(debit_acnt_dtls);
        movement.setReceiverAccountNumber(credit_acnt_dtls.getAccountNumber());
        movement.setSenderAccountNumber(debit_acnt_dtls.getAccountNumber());
        movement.setAmount(request.getAmount());
        movement.setCreatedAt(new Date());
        movement.setTransactionType(TransactionType.TRANSFER);
        movement.setTransactionalStatus(TransactionalStatus.SUCCESS);
    
        //accountRepo.save(debit_acnt_dtls);
        //accountRepo.save(credit_acnt_dtls);
        transactionRepo.save(movement);
       // throw new RuntimeException("Testing Transactional RollBack");
       idmptncy.setIdempotencyKey(IdempotencyKey);
       idmptncy.setTransactionId(movement.getTransactionID());
       idmptncy.setCreatedAt(new Date());

       idempotencyRepo.save(idmptncy);


    }
    else
    {
        throw new TransactionAlreadyProcessedException("Transaction already processed");
    }

        


    }

        
}
