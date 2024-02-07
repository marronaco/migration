package com.eviden.migration.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConnectException.class)
    public void handleConnectException(ConnectException ex){
        //almacenar respuesta de la petición
        String response = ex.getMessage();
        //mostrar respuesta
//        log.error("Error: {}", response);
    }

    /**
     * Metodo que gestiona las exepciones
     * lanzadas por WebClientResponseException
     * @param ex
     */
    @ExceptionHandler(WebClientResponseException.class)
    public void handleWebClientResponseException(WebClientResponseException ex){
        //almacenar respuesta de la petición
        String response = ex.getResponseBodyAsString();
        HttpStatus statusCode = ex.getStatusCode();
        //mostrar respuesta
        log.error("Error: {}, {}", statusCode, response);
    }

    /**
     * Metodo muestra el tipo de error lanzado
     * por la clase Authen
     * @param ex
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public void handleAuthenticationFailedException(AuthenticationFailedException ex){
        //almacenar respuesta de la petición
        String response = ex.getMessage();
        HttpStatus status = ex.getStatus();
        //mostrar respuesta
        log.error("Error autenticacion: {} {}", status, response);
    }
}
