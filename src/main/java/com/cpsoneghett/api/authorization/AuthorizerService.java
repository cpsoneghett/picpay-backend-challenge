package com.cpsoneghett.api.authorization;

import com.cpsoneghett.api.transaction.Transaction;
import com.cpsoneghett.api.transaction.exception.UnauthorizedTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthorizerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizerService.class);

    private final RestClient restClient;

    public AuthorizerService(RestClient.Builder restClient) {
        this.restClient = restClient
                .baseUrl("http://localhost:8989/authorizer")
                .build();
    }

    public void authorize(Transaction transaction) {
        LOGGER.info("Authorizing transaction {}: ", transaction.toString());
        var response = restClient.get()
                .retrieve()
                .toEntity(Authorization.class);

        if (response.getStatusCode().isError() || !response.getBody().isAuthorized())
            throw new UnauthorizedTransactionException("Unauthorized transaction!!");

        LOGGER.info("Transaction Authorized: {} ", transaction.toString());
    }
}
