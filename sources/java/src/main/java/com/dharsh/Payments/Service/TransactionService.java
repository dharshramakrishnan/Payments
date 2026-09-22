package com.dharsh.Payments.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
                 txn_hstry.setTransactionID(txn.getTransactionId());
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
                 txn_hstry.setTransactionID(txn.getTransactionId());
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
                 txn_hstry.setTransactionID(txn.getTransactionId());
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

    public TransactionHistory ProcessTransactionHistory(String TransactionId)
    {
        Optional<TransactionalModel> txn=transactionRepo.findByTransactionId(TransactionId);

        TransactionHistory txn_hst=new TransactionHistory();
        TransactionalModel txn_mdl=txn.get();
        txn_hst.setAmount(txn_mdl.getAmount());
        txn_hst.setTransactionID(txn_mdl.getTransactionId());
        txn_hst.setReceiverAccountNumber(txn_mdl.getReceiverAccountNumber());
        txn_hst.setSenderAccountNumber(txn_mdl.getSenderAccountNumber());
        txn_hst.setTransactionStatus(txn_mdl.getTransactionalStatus().toString());
        txn_hst.setTransactionType(txn_mdl.getTransactionType().toString());
        txn_hst.setTransactionTime(txn_mdl.getCreatedAt());
        txn_hst.setId(txn_mdl.getId());

        return txn_hst;



    }
   
    @Transactional
    public TransactionHistory transferAmount(String IdempotencyKey, TransferAmount request)
    {
        Optional<Idempotency> idempotency=idempotencyRepo.findByIdempotencyKey(IdempotencyKey);

        if(idempotency.isPresent())
        {
            Idempotency idmptcy=idempotency.get();
            return ProcessTransactionHistory(idmptcy.getTransactionId());

        }
      
        Idempotency idmptncy=new Idempotency();
        AccountModel debit_acnt_dtls=accountRepo.findByAccountNumber(request.getSenderAccountNumber()).orElseThrow( ()-> new AccountNotFoundException("Account" +request.getSenderAccountNumber()+"does not exists"));
        AccountModel credit_acnt_dtls=accountRepo.findByAccountNumber(request.getReceiverAccountNumber()).orElseThrow(()-> new AccountNotFoundException("Account" + request.getReceiverAccountNumber()+"does not exists"));
        

         idmptncy.setIdempotencyKey(IdempotencyKey);
         idmptncy.setCreatedAt(new Date());
         idempotencyRepo.saveAndFlush(idmptncy);


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
        movement.setTransactionId(UUID.randomUUID().toString());
        movement.setReceiverAccount(credit_acnt_dtls);
        movement.setSenderAccount(debit_acnt_dtls);
        movement.setReceiverAccountNumber(credit_acnt_dtls.getAccountNumber());
        movement.setSenderAccountNumber(debit_acnt_dtls.getAccountNumber());
        movement.setAmount(request.getAmount());
        movement.setCreatedAt(new Date());
        movement.setTransactionType(TransactionType.TRANSFER);
        movement.setTransactionalStatus(TransactionalStatus.SUCCESS);

        //returning the transaction details for successfull transfer
        TransactionHistory txn_hst=new TransactionHistory();
        txn_hst.setAmount(movement.getAmount());
        txn_hst.setTransactionID(movement.getTransactionId());
        txn_hst.setReceiverAccountNumber(movement.getReceiverAccountNumber());
        txn_hst.setSenderAccountNumber(movement.getSenderAccountNumber());
        txn_hst.setTransactionStatus(movement.getTransactionalStatus().toString());
        txn_hst.setTransactionType(movement.getTransactionType().toString());
        txn_hst.setTransactionTime(movement.getCreatedAt());
        txn_hst.setId(movement.getId());
        
    
        //accountRepo.save(debit_acnt_dtls);
        //accountRepo.save(credit_acnt_dtls);
        transactionRepo.save(movement);
       // throw new RuntimeException("Testing Transactional RollBack");
     
       idmptncy.setTransactionId(movement.getTransactionId());
      


    return txn_hst;
   

        


    }

        
}
