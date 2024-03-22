package com.cpsoneghett.api.authorization;

public class Authorization {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAuthorized() {
        return this.message.equals("Autorizado");
    }
}
