package com.cpsoneghett.api.transaction;

import com.cpsoneghett.api.wallet.Wallet;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACTIONS")
@EntityListeners(AuditingEntityListener.class)
public class Transaction implements Serializable {

    @Serial
    private static final long serialVersionUID = 6251721169894425511L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer")
    private Wallet payer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payee")
    private Wallet payee;
    private BigDecimal value;
    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public Transaction() {
        this.status = TransactionStatus.CREATED;
    }

    public Transaction(Long id, Wallet payer, Wallet payee, BigDecimal value, LocalDateTime createdAt) {
        this.id = id;
        this.payer = payer;
        this.payee = payee;
        this.value = value;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value.setScale(2, RoundingMode.DOWN);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Wallet getPayer() {
        return payer;
    }

    public void setPayer(Wallet payer) {
        this.payer = payer;
    }

    public Wallet getPayee() {
        return payee;
    }

    public void setPayee(Wallet payee) {
        this.payee = payee;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", payer=" + payer +
                ", payee=" + payee +
                ", value=" + value +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
}
