package com.dharsh.Payments.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dharsh.Payments.Exceptions.CustomerNotFoundException;
import com.dharsh.Payments.Model.CustomerModel;
import com.dharsh.Payments.Repositories.CustomerRepo;

@Service
public class CustomerService{
    @Autowired
    private CustomerRepo custRepo;
    
    ArrayList<CustomerModel>customers=new ArrayList<CustomerModel>();

    public void addCustomer(CustomerModel customer)
    {
         custRepo.save(customer);
 
    }

    public List<CustomerModel> getAllCustomers()
    {
        for(CustomerModel customer: custRepo.findAll())
        {
            customers.add(customer);
        }

        return customers;


    }

    public CustomerModel getCustomerById(long id)
    {
       return custRepo.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
    }

    public void removeCustomer(long id, CustomerModel customer)
    {   

        custRepo.deleteById(id);
    }

    public void updateCustomer(CustomerModel customer, long id)
    {
        CustomerModel existingCustomer = custRepo.findById(id).orElse(null);
        if (existingCustomer != null) {
            existingCustomer.setName(customer.getName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setPhone(customer.getPhone());
            custRepo.save(existingCustomer);
        }
    }

    

}
