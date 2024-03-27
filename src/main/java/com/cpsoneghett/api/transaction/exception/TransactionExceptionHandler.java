package com.cpsoneghett.api.transaction.exception;

import com.cpsoneghett.api.transaction.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHandler {

    private TransactionService transactionService;

    public TransactionExceptionHandler(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<Object> handle(InvalidTransactionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedTransactionException.class)
    public ResponseEntity<Object> handle(UnauthorizedTransactionException ex) {
        transactionService.saveFailedTransaction(ex.getTransaction());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
