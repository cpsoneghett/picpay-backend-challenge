package com.cpsoneghett.api.wallet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WalletExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<Object> handleWalletNotFoundException(WalletNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
