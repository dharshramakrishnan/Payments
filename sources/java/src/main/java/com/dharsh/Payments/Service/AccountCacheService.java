package com.dharsh.Payments.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dharsh.Payments.DTOs.CacheAccount;

@Service 
public class AccountCacheService {

    @Autowired 
    private StringRedisTemplate template;

    public void saveAccount(CacheAccount account)
    {
        String key="account"+account.getAccountNumber();

        template.opsForHash().put(key,"id",account.getId().toString());
        template.opsForHash().put(key,"accountNumber",account.getAccountNumber());
        template.opsForHash().put(key,"currency",account.getCurrency());
        template.opsForHash().put(key,"balance",account.getBalance().toString());
        template.opsForHash().put(key,"accountStatus",account.getStatus().name());





    }
    
}
