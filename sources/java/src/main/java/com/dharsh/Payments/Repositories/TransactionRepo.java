package com.dharsh.Payments.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dharsh.Payments.Model.TransactionalModel;

public interface TransactionRepo extends JpaRepository<TransactionalModel, Long> {
    
}
