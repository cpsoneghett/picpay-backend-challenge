CREATE TABLE IF NOT EXISTS WALLETS
(
    ID        BIGINT AUTO_INCREMENT PRIMARY KEY,
    FULL_NAME VARCHAR(100),
    CPF       VARCHAR(11),
    EMAIL     VARCHAR(100),
    PASSWORD  VARCHAR(100),
    TYPE      VARCHAR(50),
    BALANCE   DECIMAL(10, 2)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE UNIQUE INDEX cpf_idx ON WALLETS (CPF);

CREATE UNIQUE INDEX email_idx ON WALLETS (EMAIL);


CREATE TABLE IF NOT EXISTS TRANSACTIONS
(
    ID         BIGINT AUTO_INCREMENT PRIMARY KEY,
    PAYER      BIGINT,
    PAYEE      BIGINT,
    VALUE      DECIMAL(10, 2),
    STATUS     VARCHAR(20) NOT NULL,
    CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PAYER) REFERENCES WALLETS (ID),
    FOREIGN KEY (PAYEE) REFERENCES WALLETS (ID)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;