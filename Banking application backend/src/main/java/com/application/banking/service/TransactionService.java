package com.application.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.banking.dto.TransactionDTO;
import com.application.banking.model.Account;
import com.application.banking.model.Transaction;
import com.application.banking.repository.AccountRepository;
import com.application.banking.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private AccountService accountService;
    
    @Transactional
    public TransactionDTO deposit(Long accountId, BigDecimal amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }
        
        if (!account.getIsActive()) {
            throw new RuntimeException("Cannot deposit to inactive account");
        }
        
        // Create transaction
        Transaction transaction = new Transaction(account, amount, Transaction.TransactionType.DEPOSIT, description);
        transaction.setReferenceNumber(generateReferenceNumber());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        
        try {
            // Update account balance
            accountService.creditAccount(accountId, amount);
            
            // Complete transaction
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            throw new RuntimeException("Deposit failed: " + e.getMessage());
        }
        
        return new TransactionDTO(transactionRepository.save(transaction));
    }
    
    @Transactional
    public TransactionDTO withdraw(Long accountId, BigDecimal amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be positive");
        }
        
        if (!account.getIsActive()) {
            throw new RuntimeException("Cannot withdraw from inactive account");
        }
        
        // Create transaction
        Transaction transaction = new Transaction(account, amount, Transaction.TransactionType.WITHDRAWAL, description);
        transaction.setReferenceNumber(generateReferenceNumber());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        
        try {
            // Update account balance
            accountService.debitAccount(accountId, amount);
            
            // Complete transaction
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            throw new RuntimeException("Withdrawal failed: " + e.getMessage());
        }
        
        return new TransactionDTO(transactionRepository.save(transaction));
    }
    
    @Transactional
    public TransactionDTO transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found with id: " + fromAccountId));
        
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To account not found with id: " + toAccountId));
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }
        
        if (!fromAccount.getIsActive()) {
            throw new RuntimeException("From account is inactive");
        }
        
        if (!toAccount.getIsActive()) {
            throw new RuntimeException("To account is inactive");
        }
        
        if (fromAccountId.equals(toAccountId)) {
            throw new RuntimeException("Cannot transfer to the same account");
        }
        
        // Create transaction
        Transaction transaction = new Transaction(fromAccount, toAccount, amount, Transaction.TransactionType.TRANSFER, description);
        transaction.setReferenceNumber(generateReferenceNumber());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        
        try {
            // Debit from source account
            accountService.debitAccount(fromAccountId, amount);
            
            // Credit to destination account
            accountService.creditAccount(toAccountId, amount);
            
            // Complete transaction
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        } catch (Exception e) {
            transaction.setStatus(Transaction.TransactionStatus.FAILED);
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }
        
        return new TransactionDTO(transactionRepository.save(transaction));
    }
    
    public Optional<TransactionDTO> findTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(TransactionDTO::new);
    }
    
    public Optional<TransactionDTO> findTransactionByReferenceNumber(String referenceNumber) {
        return transactionRepository.findByReferenceNumber(referenceNumber)
                .map(TransactionDTO::new);
    }
    
    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findTransactionsByAccountId(accountId).stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getTransactionsByCustomerId(Long customerId) {
        return transactionRepository.findTransactionsByCustomerId(customerId).stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getTransactionsByAccountIdAndDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTransactionsByAccountIdAndDateRange(accountId, startDate, endDate).stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getTransactionsByType(Transaction.TransactionType transactionType) {
        return transactionRepository.findByTransactionType(transactionType).stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getTransactionsByStatus(Transaction.TransactionStatus status) {
        return transactionRepository.findByStatus(status).stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
    
    private String generateReferenceNumber() {
        String referenceNumber;
        do {
            referenceNumber = "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        } while (transactionRepository.existsByReferenceNumber(referenceNumber));
        
        return referenceNumber;
    }
} 