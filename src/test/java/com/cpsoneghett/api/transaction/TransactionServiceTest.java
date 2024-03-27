package com.cpsoneghett.api.transaction;

import com.cpsoneghett.api.MockUtils;
import com.cpsoneghett.api.transaction.exception.InvalidTransactionException;
import com.cpsoneghett.api.wallet.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TransactionServiceTest {

    @Autowired
    private TransactionMapper mapper;

    @Autowired
    private TransactionService transactionService;


    @Test
    public void testCreateSuccessfulTransaction() {

        Long payerId = 1L;
        Long payeeId = 2L;
        BigDecimal transactionAmount = BigDecimal.TEN;
        TransactionDTO transactionRequest = MockUtils.transactionRequest(payerId, payeeId, transactionAmount);

        // Call the service method
        TransactionDTO savedTransaction = transactionService.create(transactionRequest);

        // Assert the returned transaction
        assertNotNull(savedTransaction);

    }

    //@Test
    public void testCreateTransactionInsufficientBalance() {
        Long payerId = 1L;
        Long payeeId = 2L;
        BigDecimal amount = BigDecimal.TEN;
        TransactionDTO transaction = new TransactionDTO(1L, payerId, payeeId, amount, LocalDateTime.now());
        Wallet payerWallet = MockUtils.payerWallet();
        Wallet payeeWallet = MockUtils.payeeWallet();

        // No need to mock payeeId as transaction will fail before reaching it

        // Assert that the expected exception is thrown
        assertThrows(InvalidTransactionException.class, () -> {
            transactionService.create(transaction);
        });
    }

    //@Test
    public void testListTransactions() {
        // Mock repository to return a list of transactions
        List<Transaction> transactions = new ArrayList<>();

        // Call the service method
        List<TransactionDTO> retrievedTransactions = transactionService.list();

        // Verify the returned list
        assertEquals(transactions, retrievedTransactions);
    }


}