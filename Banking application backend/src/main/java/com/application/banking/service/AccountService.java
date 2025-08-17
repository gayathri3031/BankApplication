package com.application.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.banking.model.Account;
import com.application.banking.model.Customer;
import com.application.banking.repository.AccountRepository;
import com.application.banking.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public Account createAccount(Long customerId, Account.AccountType accountType, BigDecimal initialBalance) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        String accountNumber = generateAccountNumber();
        
        Account account = new Account(accountNumber, accountType, initialBalance, customer);
        return accountRepository.save(account);
    }
    
    public Optional<Account> findAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }
    
    public Optional<Account> findAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
    
    public List<Account> findAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerCustomerId(customerId);
    }
    
    public List<Account> findActiveAccountsByCustomerId(Long customerId) {
        return accountRepository.findActiveAccountsByCustomerId(customerId);
    }
    
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    public List<Account> getActiveAccounts() {
        return accountRepository.findByIsActiveTrue();
    }
    
    public Account updateAccountBalance(Long accountId, BigDecimal newBalance) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Account balance cannot be negative");
        }
        
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
    
    public Account creditAccount(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Credit amount must be positive");
        }
        
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }
    
    public Account debitAccount(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Debit amount must be positive");
        }
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance. Available balance: " + account.getBalance());
        }
        
        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }
    
    public Account deactivateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        account.setIsActive(false);
        return accountRepository.save(account);
    }
    
    public Account activateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        account.setIsActive(true);
        return accountRepository.save(account);
    }
    
    public BigDecimal getAccountBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        return account.getBalance();
    }
    
    public boolean accountExists(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }
    
    public long getAccountCountByCustomerId(Long customerId) {
        return accountRepository.countAccountsByCustomerId(customerId);
    }
    
    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "ACC" + String.format("%010d", new Random().nextInt(1000000000));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        
        return accountNumber;
    }
} 