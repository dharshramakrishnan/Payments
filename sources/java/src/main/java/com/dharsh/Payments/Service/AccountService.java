package com.dharsh.Payments.Service;

import java.math.BigDecimal;
import java.util.*;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.dharsh.Payments.DTOs.AccountStatusOperation;
import com.dharsh.Payments.DTOs.AmountDeposit;
import com.dharsh.Payments.DTOs.AmountWithdrawal;
import com.dharsh.Payments.DTOs.CacheAccount;
import com.dharsh.Payments.DTOs.CreateAccountRequests;
import com.dharsh.Payments.DTOs.FetchAccountDtls;
import com.dharsh.Payments.Exceptions.AccountNotFoundException;

import com.dharsh.Payments.Exceptions.CustomerNotFoundException;
import com.dharsh.Payments.Model.AccountModel;
import com.dharsh.Payments.Model.AccountStatus;
import com.dharsh.Payments.Model.CustomerModel;
import com.dharsh.Payments.Model.TransactionType;
import com.dharsh.Payments.Model.TransactionalModel;
import com.dharsh.Payments.Model.TransactionalStatus;
import com.dharsh.Payments.Repositories.AccountRepo;
import com.dharsh.Payments.Repositories.CustomerRepo;
import com.dharsh.Payments.Repositories.TransactionRepo;

@Service
public class AccountService {
    
    @Autowired
    private CustomerRepo customerRepo;
    
    @Autowired
    private AccountRepo accountRepo;

    @Autowired 
    private TransactionRepo txnRepo;

    @Autowired 
    private AccountCacheService acntCacheService;


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

    public void updateAccountStatus(AccountStatusOperation request, Long accountId)
    
    {
        AccountModel account=accountRepo.findById(accountId).orElseThrow( ()-> new AccountNotFoundException("Account with id "+accountId+"not found"));
       
        AccountStatus currentStatus=account.getStatus() ;

        if(currentStatus==AccountStatus.CLOSED)
        {
            throw new IllegalStateException("Cannot CLOSE an Account that is already CLOSED");
        }

        if(currentStatus==AccountStatus.ACTIVE && request.getStatus()==AccountStatus.CLOSED)
        {
            throw new IllegalStateException("Cannot close an Active account, please ensure that the account is blocked before closing");
        }

        account.setStatus(request.getStatus());
        accountRepo.save(account);

    
    }

    public void depositAmount(AmountDeposit request, Long id)
    {
        AccountModel account= accountRepo.findById(id).orElseThrow(()-> new AccountNotFoundException("Account with id"+ id + "not found"));
        if(account.getStatus()==AccountStatus.CLOSED || account.getStatus()==AccountStatus.BLOCKED)
        {
            throw new IllegalStateException("Cannot deposit  amount in closed or blocked account");
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepo.save(account);

        //create a transaction record for the deposit operation
        TransactionalModel txn=new TransactionalModel();

        txn.setTransactionId(UUID.randomUUID().toString());
        txn.setAmount(request.getAmount());
        txn.setTransactionType(TransactionType.DEPOSIT);
        txn.setTransactionalStatus(TransactionalStatus.SUCCESS);
        //txn.setSenderAccountID(account);
        txn.setReceiverAccount(account);
        //txn.setSenderAccountNumber(request.getSenderAccountNumber());
        txn.setReceiverAccountNumber(account.getAccountNumber());
        txn.setCreatedAt(new Date());

        txnRepo.save(txn);


        
        


    }

    public void withdrawAmount(AmountWithdrawal request, Long id)
    {
        AccountModel account= accountRepo.findById(id).orElseThrow(()-> new AccountNotFoundException("Account with id" + id +"not found"));
        if(account.getStatus()==AccountStatus.CLOSED || account.getStatus()==AccountStatus.BLOCKED)
        {
            throw new IllegalStateException("Cannot withdraw amount from closed or blocked account");
        }

        if(account.getBalance().compareTo(request.getAmount())<0)
        {
            throw new IllegalStateException("Insufficient balance in the account");

        }


        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepo.save(account);

        
        //create a transaction record for the Withdrawal  operation
        TransactionalModel txn=new TransactionalModel();

        txn.setTransactionId(UUID.randomUUID().toString());
        txn.setAmount(request.getAmount());
        txn.setTransactionType(TransactionType.WITHDRAWAL);
        txn.setTransactionalStatus(TransactionalStatus.SUCCESS);
        txn.setSenderAccount(account);
        txn.setSenderAccountNumber(account.getAccountNumber());
        //txn.setReceiverAccount(account);
        txn.setCreatedAt(new Date());

        txnRepo.save(txn);
    }

    public FetchAccountDtls getAccountByAccountNumber(String accountNumber)
    {
        FetchAccountDtls account=new FetchAccountDtls();
        CacheAccount cache_acnt=acntCacheService.getCacheAccount(accountNumber);
        if(cache_acnt!=null)
        {
            account.setAccountNumber(cache_acnt.getAccountNumber());
            account.setAccountStatus(cache_acnt.getStatus().toString());
            account.setBalance(cache_acnt.getBalance().toString());
            account.setCurrency(cache_acnt.getCurrency());
            account.setId(cache_acnt.getId().toString());

            System.out.print("HIT");

        }
        else{
             
            Optional<AccountModel> acnt_model=accountRepo.findByAccountNumber(accountNumber);
            

            if(acnt_model.isPresent())
            {
                account.setAccountNumber(acnt_model.get().getAccountNumber());
                account.setAccountStatus(acnt_model.get().getStatus().toString());
                account.setBalance(acnt_model.get().getBalance().toString());
                account.setCurrency(acnt_model.get().getCurrency());
                account.setId(acnt_model.get().getId().toString());

                CacheAccount cache_account=new CacheAccount();
                cache_account.setAccountNumber(acnt_model.get().getAccountNumber());
                cache_account.setBalance(acnt_model.get().getBalance());
                cache_account.setCurrency(acnt_model.get().getCurrency());
                cache_account.setId(acnt_model.get().getId());
                cache_account.setStatus(acnt_model.get().getStatus());

                acntCacheService.saveAccount(cache_account);

                System.out.print("MISS");

            }
            else
            {
                throw new AccountNotFoundException("Account with account number : "+accountNumber+"  does not exists");
            }
             
        }

        
        return account;
    }

    


}
