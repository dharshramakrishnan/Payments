package com.dharsh.Payments.Service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dharsh.Payments.DTOs.CacheAccount;
import com.dharsh.Payments.Model.AccountStatus;

@Service 
public class AccountCacheService {

    @Autowired 
    private StringRedisTemplate template;

    public void saveAccount(CacheAccount account)
    {
        String key="account:"+account.getAccountNumber();

        template.opsForHash().put(key,"id",account.getId().toString());
        template.opsForHash().put(key,"accountNumber",account.getAccountNumber());
        template.opsForHash().put(key,"currency",account.getCurrency());
        template.opsForHash().put(key,"balance",account.getBalance().toString());
        template.opsForHash().put(key,"accountStatus",account.getStatus().name());

        System.out.println(account.getId().toString());
        System.out.println(account.getAccountNumber().toString());
        System.out.println(account.getCurrency().toString());
        System.out.println(account.getBalance().toString());
        System.out.println(account.getStatus().toString());



    }
    
    public CacheAccount getCacheAccount(String accountNumber)
    {
        String acnt_num="account:"+accountNumber;
        Map<Object,Object> mpp=template.opsForHash().entries(acnt_num);
        
        if(mpp.isEmpty())
        {
            return null;
        }

        CacheAccount cache_acnt=new CacheAccount();
      
        cache_acnt.setAccountNumber(mpp.get("accountNumber").toString());
        cache_acnt.setId(Long.valueOf(mpp.get("id").toString()));
        cache_acnt.setBalance(new BigDecimal(mpp.get("balance").toString()));
        cache_acnt.setStatus(AccountStatus.valueOf(mpp.get("accountStatus").toString()));
        cache_acnt.setCurrency(mpp.get("currency").toString());

        for(Map.Entry<Object,Object> map : mpp.entrySet())
        {
            System.out.println(map.getKey()+" : "+map.getValue());
        }

        return cache_acnt;

    }
}
