package com.cpsoneghett.api.wallet;

import com.cpsoneghett.api.transaction.Transaction;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "WALLETS")
public class Wallet implements Serializable {

    @Serial
    private static final long serialVersionUID = -7747749590976330791L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FULL_NAME")
    private String fullName;
    private String cpf;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private WalletType type;
    private BigDecimal balance;

    @OneToMany(mappedBy = "payer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionsPayer;

    @OneToMany(mappedBy = "payee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactionsPayee;

    public Wallet(Long id, String fullName, String cpf, String email, String password, WalletType type, BigDecimal balance) {
        this.id = id;
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.type = type;
        this.balance = balance;
    }

    public Wallet() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WalletType getType() {
        return type;
    }

    public void setType(WalletType type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionsPayer() {
        return transactionsPayer;
    }

    public void setTransactionsPayer(List<Transaction> transactionsPayer) {
        this.transactionsPayer = transactionsPayer;
    }

    public List<Transaction> getTransactionsPayee() {
        return transactionsPayee;
    }

    public void setTransactionsPayee(List<Transaction> transactionsPayee) {
        this.transactionsPayee = transactionsPayee;
    }

    public Wallet debit(BigDecimal value) {
        this.setBalance(this.getBalance().subtract(value));
        return this;
    }

    public Wallet credit(BigDecimal value) {
        this.setBalance(this.getBalance().add(value));
        return this;
    }


}
