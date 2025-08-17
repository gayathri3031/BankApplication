package com.application.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.banking.model.Account;
import com.application.banking.model.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findByCustomer(Customer customer);
    
    List<Account> findByCustomerCustomerId(Long customerId);
    
    List<Account> findByAccountType(Account.AccountType accountType);
    
    List<Account> findByIsActiveTrue();
    
    List<Account> findByCustomerAndIsActiveTrue(Customer customer);
    
    @Query("SELECT a FROM Account a WHERE a.customer.customerId = :customerId AND a.isActive = true")
    List<Account> findActiveAccountsByCustomerId(@Param("customerId") Long customerId);
    
    boolean existsByAccountNumber(String accountNumber);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.customer.customerId = :customerId")
    long countAccountsByCustomerId(@Param("customerId") Long customerId);
} 