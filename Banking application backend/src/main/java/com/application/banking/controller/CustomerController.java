package com.application.banking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.banking.dto.CustomerCreateRequest;
import com.application.banking.model.Customer;
import com.application.banking.service.CustomerService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
@Tag(name = "Customer Management", description = "APIs for managing customer information")
@SecurityRequirement(name = "basicAuth")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid customer data provided")
    })
    public ResponseEntity<?> createCustomer(
            @Parameter(description = "Customer information to create") @Valid @RequestBody CustomerCreateRequest request) {
        try {
            // Convert DTO to Entity
            Customer customer = new Customer(request.getFirstName(), request.getLastName(), 
                                           request.getEmail(), request.getPhoneNumber(), request.getAddress());
            Customer createdCustomer = customerService.createCustomer(customer);
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId) {
        Optional<Customer> customer = customerService.findCustomerById(customerId);
        if (customer.isPresent()) {
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Customer not found with id: " + customerId, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> customer = customerService.findCustomerByEmail(email);
        if (customer.isPresent()) {
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Customer not found with email: " + email, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomersByName(@RequestParam String name) {
        List<Customer> customers = customerService.findCustomersByName(name);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long customerId, 
                                          @Valid @RequestBody CustomerCreateRequest request) {
        try {
            // Convert DTO to Entity
            Customer customerDetails = new Customer(request.getFirstName(), request.getLastName(), 
                                                  request.getEmail(), request.getPhoneNumber(), request.getAddress());
            Customer updatedCustomer = customerService.updateCustomer(customerId, customerDetails);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{customerId}/exists")
    public ResponseEntity<Boolean> customerExists(@PathVariable Long customerId) {
        boolean exists = customerService.customerExists(customerId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    @GetMapping("/email/{email}/exists")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = customerService.emailExists(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    @GetMapping("/phone/{phoneNumber}/exists")
    public ResponseEntity<Boolean> phoneExists(@PathVariable String phoneNumber) {
        boolean exists = customerService.phoneExists(phoneNumber);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
} 