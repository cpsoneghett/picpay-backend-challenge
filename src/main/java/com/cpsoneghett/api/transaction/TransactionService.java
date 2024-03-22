package com.cpsoneghett.api.transaction;

import com.cpsoneghett.api.authorization.AuthorizerService;
import com.cpsoneghett.api.notification.NotificationService;
import com.cpsoneghett.api.wallet.Wallet;
import com.cpsoneghett.api.wallet.WalletRepository;
import com.cpsoneghett.api.wallet.WalletType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizerService authorizerService, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transaction create(Transaction transaction) {

        validate(transaction);

        var newTransaction = transactionRepository.save(transaction);

        var walletPayer = walletRepository.findById(transaction.getPayer()).get();
        var walletPayee = walletRepository.findById(transaction.getPayee()).get();

        walletRepository.save(walletPayer.debit(transaction.getValue()));
        walletRepository.save(walletPayee.credit(transaction.getValue()));

        //4-call external services

        //authorize transaction
        authorizerService.authorize(transaction);

        //notify
        notificationService.notify(transaction);

        return newTransaction;
    }

    /**
     * 1- The payer has a common wallet;
     * 2- The payer has enough balance;
     * 3- The payer is not the payee;
     */
    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.getPayee())
                .map(payee -> walletRepository.findById(transaction.getPayer()).map(payer ->
                        isTransactionValid(transaction, payer) ? transaction : null
                ).orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction.toString()))))
                .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction.toString())));

    }

    private boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.getType() == WalletType.DEFAULT.getValue() &&
                payer.getBalance().compareTo(transaction.getValue()) >= 0 &&
                !payer.getId().equals(transaction.getPayee());
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }
}
