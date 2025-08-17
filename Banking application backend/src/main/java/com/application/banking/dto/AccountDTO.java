package com.application.banking.dto;

public class AccountDTO {
    private Long accountId;
    private String accountNumber;
    private String accountType;

    public AccountDTO(Long accountId, String accountNumber, String accountType) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    // Getters and Setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
} 