package com.application.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.application.banking.model.Transaction;

public class TransactionDTO {
    private Long transactionId;
    private String referenceNumber;
    private String transactionType;
    private BigDecimal amount;
    private String status;
    private LocalDateTime transactionDate;
    private String description;
    private AccountDTO fromAccount;
    private AccountDTO toAccount;

    public TransactionDTO(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.referenceNumber = transaction.getReferenceNumber();
        this.transactionType = transaction.getTransactionType().name();
        this.amount = transaction.getAmount();
        this.status = transaction.getStatus().name();
        this.transactionDate = transaction.getTransactionDate();
        this.description = transaction.getDescription();
        
        if (transaction.getFromAccount() != null) {
            this.fromAccount = new AccountDTO(
                transaction.getFromAccount().getAccountId(),
                transaction.getFromAccount().getAccountNumber(),
                transaction.getFromAccount().getAccountType().name()
            );
        }
        
        if (transaction.getToAccount() != null) {
            this.toAccount = new AccountDTO(
                transaction.getToAccount().getAccountId(),
                transaction.getToAccount().getAccountNumber(),
                transaction.getToAccount().getAccountType().name()
            );
        }
    }

    // Getters and Setters
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AccountDTO getFromAccount() { return fromAccount; }
    public void setFromAccount(AccountDTO fromAccount) { this.fromAccount = fromAccount; }
    public AccountDTO getToAccount() { return toAccount; }
    public void setToAccount(AccountDTO toAccount) { this.toAccount = toAccount; }
} 