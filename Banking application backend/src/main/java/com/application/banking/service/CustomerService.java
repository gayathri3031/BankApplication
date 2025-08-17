package com.application.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.banking.model.Customer;
import com.application.banking.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public Customer createCustomer(Customer customer) {
        // Check if email already exists
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Customer with email " + customer.getEmail() + " already exists");
        }
        
        // Check if phone number already exists
        if (customer.getPhoneNumber() != null && customerRepository.existsByPhoneNumber(customer.getPhoneNumber())) {
            throw new RuntimeException("Customer with phone number " + customer.getPhoneNumber() + " already exists");
        }
        
        return customerRepository.save(customer);
    }
    
    public Optional<Customer> findCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }
    
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    
    public List<Customer> findCustomersByName(String name) {
        return customerRepository.findByNameContaining(name);
    }
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    public Customer updateCustomer(Long customerId, Customer customerDetails) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        // Check if email is being changed and if new email already exists
        if (!customer.getEmail().equals(customerDetails.getEmail()) && 
            customerRepository.existsByEmail(customerDetails.getEmail())) {
            throw new RuntimeException("Customer with email " + customerDetails.getEmail() + " already exists");
        }
        
        // Check if phone number is being changed and if new phone number already exists
        if (customerDetails.getPhoneNumber() != null && 
            !customerDetails.getPhoneNumber().equals(customer.getPhoneNumber()) &&
            customerRepository.existsByPhoneNumber(customerDetails.getPhoneNumber())) {
            throw new RuntimeException("Customer with phone number " + customerDetails.getPhoneNumber() + " already exists");
        }
        
        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhoneNumber(customerDetails.getPhoneNumber());
        customer.setAddress(customerDetails.getAddress());
        
        return customerRepository.save(customer);
    }
    
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        customerRepository.delete(customer);
    }
    
    public boolean customerExists(Long customerId) {
        return customerRepository.existsById(customerId);
    }
    
    public boolean emailExists(String email) {
        return customerRepository.existsByEmail(email);
    }
    
    public boolean phoneExists(String phoneNumber) {
        return customerRepository.existsByPhoneNumber(phoneNumber);
    }
} 