package com.cpsoneghett.api.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(Long idTransaction,
                             @JsonProperty("payer") Long payerId,
                             @JsonProperty("payee") Long payeeId,
                             BigDecimal value,
                             LocalDateTime createdAt,
                             TransactionStatus status) {
}
