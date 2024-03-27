package com.cpsoneghett.api;

import com.cpsoneghett.api.transaction.Transaction;
import com.cpsoneghett.api.transaction.TransactionDTO;
import com.cpsoneghett.api.wallet.Wallet;
import com.cpsoneghett.api.wallet.WalletType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MockUtils {

    public static Wallet payerWallet() {
        return new Wallet(1L, "Payer Wallet", "11122233300", "payerId@email.com", "1234", WalletType.CUSTOMER, BigDecimal.valueOf(100));
    }

    public static Wallet payeeWallet() {
        return new Wallet(2L, "Payee Wallet", "12345678900", "payeeId@email.com", "1234", WalletType.STORE, BigDecimal.ZERO);
    }

    public static TransactionDTO transactionRequest(Long payerId, Long payeeId, BigDecimal transactionAmount) {
        return new TransactionDTO(null, payerId, payeeId, transactionAmount, null);
    }

    public static Transaction transaction(Wallet payer, Wallet payee, BigDecimal transactionAmount) {
        return new Transaction(1L, payer, payee, transactionAmount, LocalDateTime.now());
    }
}
