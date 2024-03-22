package com.cpsoneghett.api.wallet;

public enum WalletType {
    DEFAULT(1), STORE(2);

    private int value;

    WalletType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
