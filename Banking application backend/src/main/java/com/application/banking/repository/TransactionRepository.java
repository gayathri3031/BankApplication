package com.application.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.banking.model.Account;
import com.application.banking.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByFromAccount(Account fromAccount);
    
    List<Transaction> findByToAccount(Account toAccount);
    
    List<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount);
    
    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);
    
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.accountId = :accountId OR t.toAccount.accountId = :accountId ORDER BY t.transactionDate DESC")
    List<Transaction> findTransactionsByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount.accountId = :accountId OR t.toAccount.accountId = :accountId) AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findTransactionsByAccountIdAndDateRange(@Param("accountId") Long accountId, 
                                                            @Param("startDate") LocalDateTime startDate, 
                                                            @Param("endDate") LocalDateTime endDate);
    
    Optional<Transaction> findByReferenceNumber(String referenceNumber);
    
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.customer.customerId = :customerId OR t.toAccount.customer.customerId = :customerId ORDER BY t.transactionDate DESC")
    List<Transaction> findTransactionsByCustomerId(@Param("customerId") Long customerId);
    
    boolean existsByReferenceNumber(String referenceNumber);
} 