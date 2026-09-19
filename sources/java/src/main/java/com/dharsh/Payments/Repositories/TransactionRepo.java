package com.dharsh.Payments.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dharsh.Payments.Model.TransactionalModel;
@Repository 
public interface TransactionRepo extends JpaRepository<TransactionalModel, Long> {
       
}
