package com.cpsoneghett.api.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionRequest) {
        TransactionDTO newTransaction = transactionService.create(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> list() {
        List<TransactionDTO> response = transactionService.list();
        return ResponseEntity.ok(response);
    }
}
