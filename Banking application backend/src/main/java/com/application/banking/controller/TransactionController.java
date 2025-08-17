package com.application.banking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.banking.dto.TransactionDTO;
import com.application.banking.model.Transaction;
import com.application.banking.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
@Tag(name = "Transaction Management", description = "APIs for banking transactions including deposits, withdrawals, and transfers")
@SecurityRequirement(name = "basicAuth")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping("/deposit")
    @Operation(summary = "Make a deposit", description = "Deposits money into the specified account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Deposit successful"),
        @ApiResponse(responseCode = "400", description = "Invalid deposit request")
    })
    public ResponseEntity<TransactionDTO> deposit(
            @Parameter(description = "Account ID to deposit to") @RequestParam Long accountId,
            @Parameter(description = "Amount to deposit") @RequestParam BigDecimal amount,
            @Parameter(description = "Description of the deposit") @RequestParam(defaultValue = "Deposit") String description) {
        try {
            TransactionDTO transaction = transactionService.deposit(accountId, amount, description);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@RequestParam Long accountId,
                                    @RequestParam BigDecimal amount,
                                    @RequestParam(defaultValue = "Withdrawal") String description) {
        try {
            TransactionDTO transaction = transactionService.withdraw(accountId, amount, description);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> transfer(@RequestParam Long fromAccountId,
                                    @RequestParam Long toAccountId,
                                    @RequestParam BigDecimal amount,
                                    @RequestParam(defaultValue = "Transfer") String description) {
        try {
            TransactionDTO transaction = transactionService.transfer(fromAccountId, toAccountId, amount, description);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all transactions")
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get a transaction by its ID")
    @ApiResponse(responseCode = "200", description = "Transaction found")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return transactionService.findTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/reference/{referenceNumber}")
    public ResponseEntity<?> getTransactionByReferenceNumber(@PathVariable String referenceNumber) {
        Optional<TransactionDTO> transaction = transactionService.findTransactionByReferenceNumber(referenceNumber);
        if (transaction.isPresent()) {
            return new ResponseEntity<>(transaction.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Transaction not found with reference number: " + referenceNumber, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountId(accountId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCustomerId(@PathVariable Long customerId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByCustomerId(customerId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/account/{accountId}/date-range")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountIdAndDateRange(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountIdAndDateRange(accountId, startDate, endDate);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/type/{transactionType}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByType(@PathVariable Transaction.TransactionType transactionType) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByType(transactionType);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByStatus(@PathVariable Transaction.TransactionStatus status) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByStatus(status);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
} 