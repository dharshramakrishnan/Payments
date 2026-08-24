package com.dharsh.Payments.Repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dharsh.Payments.Model.CustomerModel;
@Repository
public interface CustomerRepo extends JpaRepository<CustomerModel, Long> {
    
    

}
