package com.eviden.migration.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthenticationFailedException extends RuntimeException{

    private HttpStatus status;

    public AuthenticationFailedException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
