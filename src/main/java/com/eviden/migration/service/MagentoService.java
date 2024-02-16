package com.eviden.migration.service;

import com.eviden.migration.exceptions.AuthenticationFailedException;
import com.eviden.migration.exceptions.ResourceNotFoundException;
import com.eviden.migration.model.DrupalProductoCsv;
import com.eviden.migration.model.request.MagentoAuthRequest;
import com.eviden.migration.model.response.MagentoAuthToken;
import com.eviden.migration.model.request.MagentoMedia;
import com.eviden.migration.model.request.MagentoProducto;
import com.eviden.migration.model.response.MagentoMediaResponse;
import com.eviden.migration.model.response.MagentoProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static com.eviden.migration.utils.MagentoMediaMapper.*;
import static com.eviden.migration.utils.MagentoProductMapper.mapDrupalProductoDetalleToMagento;
@Slf4j
@Service
public class MagentoService {
    private final WebClient magentoWebClient;
    private static String  authToken;

    public MagentoService(WebClient magentoWebClient) {
        this.magentoWebClient = magentoWebClient;
    }

    /**
     * Autenticacion de usuario, obtener token
     */
    public Mono<MagentoAuthToken> autenticarUsuario(){
        log.info("Magento: autenticacion del usuario, obteniendo token...");
        //definir credenciales del usuario y contraseña
        MagentoAuthRequest authRequest = MagentoAuthRequest.builder()
                .username("DrupalAdmin")
                .password("B1llMurr@y")
                .build();

        //almacenar el token devuelto de la solicitud
        return magentoWebClient.post()
                .uri("/integration/admin/token")
                .body(Mono.just(authRequest), MagentoAuthRequest.class)
                .retrieve()
                .bodyToMono(MagentoAuthToken.class)
                .doOnSuccess(magentoAuthToken -> {
                    log.info("Magento: token recibido almacenado");
                    // Almacenar el token obtenido en la variable
                    authToken = magentoAuthToken.getToken();
                });
    }

    /**
     * Metodo para insertar producto en magento
     *
     * @param drupalProducto
     */
    public Mono<MagentoProductResponse> insertarProducto(DrupalProductoCsv drupalProducto) {
        //comprobar si el token es nulo
        if(authToken == null){
            throw new AuthenticationFailedException("Token es nulo", HttpStatus.BAD_REQUEST);
        }
        log.info("Magento: insertando producto {}", drupalProducto.getSku());
        //mapear el producto de tipo drupal a magento
        MagentoProducto magentoProducto = mapDrupalProductoDetalleToMagento(drupalProducto);
        //enviar peticion HTTP a la API Magento
        return magentoWebClient.post()
                .uri("/products")
                .header("Authorization", "Bearer %s".formatted(authToken))
                .body(Mono.just(magentoProducto), MagentoProducto.class)
                .retrieve()
                .bodyToMono(MagentoProductResponse.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    //manejo detallado de la excepcion
                    HttpStatus status = ex.getStatusCode();
                    String response = ex.getResponseBodyAsString();
                    return Mono.error(
                            new ResourceNotFoundException("Error: '%s' en producto '%s'  | Mensaje: '%s'"
                                    .formatted(status,drupalProducto.getSku(), response)));
                })
                .doOnError(error -> {
                    log.error("Error: '%s'".formatted(error));
                });
    }

    public Mono<MagentoMediaResponse> insertarImagenEnProducto(String imagePath, String productoSku){
        log.info("Magento: insertando imagen en el producto {}", productoSku);
        //mapear la ruta de imagen a MagentoMedia
        MagentoMedia magentoMedia = mapDrupalMedia(imagePath);
        return magentoWebClient.post()
                .uri("/products/{productoName}/media", productoSku)
                .header("Authorization", "Bearer %s".formatted(authToken))
                .body(Mono.just(magentoMedia), MagentoMedia.class)
                .retrieve()
                .bodyToMono(MagentoMediaResponse.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    //manejo detallado de la excepcion
                    HttpStatus status = ex.getStatusCode();
                    String response = ex.getResponseBodyAsString();
                    return Mono.error(
                            new ResourceNotFoundException("Error: '%s' en producto '%s' | Mensaje: '%s'"
                                    .formatted(status,productoSku, response)));
                })
                .doOnError(error -> {
                    log.error("Error: '%s'".formatted(error));
                });

    }
}
