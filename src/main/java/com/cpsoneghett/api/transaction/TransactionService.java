package com.cpsoneghett.api.transaction;

import com.cpsoneghett.api.authorization.AuthorizerService;
import com.cpsoneghett.api.notification.NotificationService;
import com.cpsoneghett.api.transaction.exception.InvalidTransactionException;
import com.cpsoneghett.api.transaction.exception.UnauthorizedTransactionException;
import com.cpsoneghett.api.wallet.Wallet;
import com.cpsoneghett.api.wallet.WalletNotFoundException;
import com.cpsoneghett.api.wallet.WalletRepository;
import com.cpsoneghett.api.wallet.WalletType;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;
    private final TransactionMapper mapper;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizerService authorizerService, NotificationService notificationService, TransactionMapper mapper) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    @Transactional(dontRollbackOn = UnauthorizedTransactionException.class)
    public TransactionDTO create(TransactionDTO transactionDto) {

        Transaction transaction = validate(transactionDto);

        var walletPayer = transaction.getPayer();
        var walletPayee = transaction.getPayee();

        walletRepository.save(walletPayer.debit(transactionDto.value()));
        walletRepository.save(walletPayee.credit(transactionDto.value()));

        transaction.setStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(transaction);

        authorizerService.authorize(transaction);

        notificationService.notify(mapper.modelToDto(transaction));

        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);

        return mapper.modelToDto(transaction);
    }

    /**
     * 1- The payerId has a common wallet;
     * 2- The payerId has enough balance;
     * 3- The payerId is not the payeeId;
     */
    private Transaction validate(TransactionDTO transactionDto) {

        LOGGER.info("validating transaction {}...", transactionDto);

        Wallet payeeWallet = walletRepository.findById(transactionDto.payeeId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found. Id: %l".formatted(transactionDto.payeeId())));
        Wallet payerWallet = walletRepository.findById(transactionDto.payerId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found. Id: %l".formatted(transactionDto.payerId())));

        if (!isTransactionValid(transactionDto, payerWallet))
            throw new InvalidTransactionException("Invalid transaction - %s".formatted(transactionDto.toString()));

        Transaction transaction = mapper.dtoToModel(transactionDto);
        transaction.setStatus(TransactionStatus.CREATED);
        transaction.setPayee(payeeWallet);
        transaction.setPayer(payerWallet);

        return transactionRepository.save(transaction);
    }

    private boolean isTransactionValid(TransactionDTO transaction, Wallet payer) {
        return payer.getType() == WalletType.CUSTOMER &&
                payer.getBalance().compareTo(transaction.value()) >= 0 &&
                !payer.getId().equals(transaction.payeeId());
    }

    public List<TransactionDTO> list() {
        List<Transaction> transactions = transactionRepository.findAll();
        return mapper.map(transactions);
    }

    public void saveFailedTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.save(transaction);
    }
}
