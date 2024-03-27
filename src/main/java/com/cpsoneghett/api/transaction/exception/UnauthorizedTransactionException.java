package com.cpsoneghett.api.transaction.exception;

import com.cpsoneghett.api.transaction.Transaction;

public class UnauthorizedTransactionException extends RuntimeException {

    private Transaction transaction;

    public UnauthorizedTransactionException(String message) {
        super(message);
    }

    public UnauthorizedTransactionException(Transaction t, String message) {
        super(message);
        this.transaction = t;
    }

    public Transaction getTransaction() {
        return transaction;
    }

}
