package com.dharsh.Payments.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dharsh.Payments.Model.Idempotency;

@Repository 
public interface IdempotencyRepo extends JpaRepository<Idempotency,Integer> {
    
    Optional<Idempotency> findByIdempotencyKey(String idempotencyKey);
}
