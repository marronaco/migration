package com.eviden.migration.service;

import com.eviden.migration.exceptions.AuthenticationFailedException;
import com.eviden.migration.exceptions.ResourceNotFoundException;
import com.eviden.migration.model.response.DrupalAuthCsrf;
import com.eviden.migration.model.response.DrupalProducto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Service
public class DrupalService {
    private final WebClient drupalWebClient;

    public DrupalService(WebClient drupalWebClient) {
        this.drupalWebClient = drupalWebClient;
    }

    /**
     * Metodo para obtner el token de CSRF
     * @return token CSRF
     */
    public Mono<DrupalAuthCsrf> obtenerDrupalCSRF(){
        log.info("Drupal: iniciando autenticacion CSRF...");
        //obtner el CSRF de drupal
        return drupalWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/user/token.json").build())
                .retrieve()
                .bodyToMono(DrupalAuthCsrf.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    //manejo detallado de la excepcion
                    HttpStatus status = ex.getStatusCode();
                    String response = ex.getResponseBodyAsString();
                    return Mono.error(
                            new AuthenticationFailedException(response,status));
                })
                .doOnError(error -> {
                    log.error("Error: '%s'".formatted(error));
                });
    }

    /**
     * Metodo que obtiene un detalle del producto segun NID
     * @param nid
     * @return producto
     */
    public Mono<DrupalProducto> obtenerProductoByNid(Integer nid){
        log.info("Drupal: obteniendo producto del NID '{}'", nid);
        return drupalWebClient.get()
                .uri("/node/{nid}",nid)
                .retrieve()
                .bodyToMono(DrupalProducto.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    //manejo detallado de la excepcion
                    HttpStatus status = ex.getStatusCode();
                    String response = ex.getResponseBodyAsString();
                    return Mono.error(
                            new ResourceNotFoundException("Error: '%s' | Mensaje: '%s'"
                                    .formatted(status,response)));
                })
                .doOnError(error -> {
                    log.error("Error: '%s'".formatted(error));
                });
    }



    
}
