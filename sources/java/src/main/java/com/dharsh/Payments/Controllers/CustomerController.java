package com.dharsh.Payments.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dharsh.Payments.Model.CustomerModel;
import com.dharsh.Payments.Service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class CustomerController {

    @Autowired
    private CustomerService custService;
    
      @PostMapping("/addCustomer")
      public String addCustomer(@Valid @RequestBody CustomerModel customer)
      {
        custService.addCustomer(customer);
        return new ResponseEntity<>("Customer Added Successfully", HttpStatus.CREATED).toString();
      }

      @GetMapping("/getAllCustomers")
      public List<CustomerModel> getAllCustomers()
      {
         return custService.getAllCustomers();
      }
      
      @GetMapping("/getCustomerById/{id}")
      public CustomerModel getCustomerById(@PathVariable long id)
      {
         return custService.getCustomerById(id);
      }
      
      
      @DeleteMapping("/removeCustomer/{id}")
      public String removeCustomer(@PathVariable long id, @RequestBody CustomerModel customer)
      {
        custService.removeCustomer(id,customer);
        return new ResponseEntity<>("Removed Customer Successfully", HttpStatus.OK).toString();
      }
      
      @PutMapping("/updateCustomer/{id}")
      public String updateCustomer(@PathVariable long id, @RequestBody CustomerModel customer)
      {
         custService.updateCustomer(customer,id);
         return new ResponseEntity<>("Customer Updated Successfully", HttpStatus.OK).toString();
      }

      
      

}
