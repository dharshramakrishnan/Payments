package com.dharsh.Payments.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dharsh.Payments.Model.AccountModel;

@Repository
public interface AccountRepo extends JpaRepository<AccountModel, Long> {

    Optional<AccountModel> findByAccountNumber(String accountNumber);
    
}
