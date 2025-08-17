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

import com.application.banking.model.Account;
import com.application.banking.service.AccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
@SecurityRequirement(name = "basicAuth")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestParam Long customerId,
                                         @RequestParam Account.AccountType accountType,
                                         @RequestParam(defaultValue = "0.0") BigDecimal initialBalance) {
        try {
            Account createdAccount = accountService.createAccount(customerId, accountType, initialBalance);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Account>> getActiveAccounts() {
        List<Account> accounts = accountService.getActiveAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
    
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Long accountId) {
        Optional<Account> account = accountService.findAccountById(accountId);
        if (account.isPresent()) {
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found with id: " + accountId, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<?> getAccountByNumber(@PathVariable String accountNumber) {
        Optional<Account> account = accountService.findAccountByNumber(accountNumber);
        if (account.isPresent()) {
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found with number: " + accountNumber, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Account>> getAccountsByCustomerId(@PathVariable Long customerId) {
        List<Account> accounts = accountService.findAccountsByCustomerId(customerId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
    
    @GetMapping("/customer/{customerId}/active")
    public ResponseEntity<List<Account>> getActiveAccountsByCustomerId(@PathVariable Long customerId) {
        List<Account> accounts = accountService.findActiveAccountsByCustomerId(customerId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
    
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getAccountBalance(@PathVariable Long accountId) {
        try {
            BigDecimal balance = accountService.getAccountBalance(accountId);
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{accountId}/balance")
    public ResponseEntity<?> updateAccountBalance(@PathVariable Long accountId,
                                                @RequestParam BigDecimal newBalance) {
        try {
            Account updatedAccount = accountService.updateAccountBalance(accountId, newBalance);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/{accountId}/credit")
    public ResponseEntity<?> creditAccount(@PathVariable Long accountId,
                                         @RequestParam BigDecimal amount) {
        try {
            Account updatedAccount = accountService.creditAccount(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/{accountId}/debit")
    public ResponseEntity<?> debitAccount(@PathVariable Long accountId,
                                        @RequestParam BigDecimal amount) {
        try {
            Account updatedAccount = accountService.debitAccount(accountId, amount);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{accountId}/deactivate")
    public ResponseEntity<?> deactivateAccount(@PathVariable Long accountId) {
        try {
            Account updatedAccount = accountService.deactivateAccount(accountId);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{accountId}/activate")
    public ResponseEntity<?> activateAccount(@PathVariable Long accountId) {
        try {
            Account updatedAccount = accountService.activateAccount(accountId);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/number/{accountNumber}/exists")
    public ResponseEntity<Boolean> accountExists(@PathVariable String accountNumber) {
        boolean exists = accountService.accountExists(accountNumber);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    @GetMapping("/customer/{customerId}/count")
    public ResponseEntity<Long> getAccountCountByCustomerId(@PathVariable Long customerId) {
        long count = accountService.getAccountCountByCustomerId(customerId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
} 