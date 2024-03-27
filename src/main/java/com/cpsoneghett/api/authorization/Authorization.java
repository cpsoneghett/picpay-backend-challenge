package com.cpsoneghett.api.authorization;

public record Authorization(String message) {

    public boolean isAuthorized() {
        return this.message.equals("Authorized");
    }
}
