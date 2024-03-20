package com.eviden.migration.service;

import com.eviden.migration.exceptions.AuthenticationFailedException;
import com.eviden.migration.exceptions.ResourceNotFoundException;
import com.eviden.migration.models.drupal.DrupalProducto;
import com.eviden.migration.models.magento.MagentoAuthRequest;
import com.eviden.migration.models.magento.MagentoAuthToken;
import com.eviden.migration.models.magento.MagentoProductResponse;
import com.eviden.migration.models.magento.MagentoProducto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static com.eviden.migration.utils.MagentoProductMapper.mapDrupalProductoDetalleToMagento;

/**
 * Servicio que gestionan la inserción
 * de productos en magento
 */
@Slf4j
@Service
public class MagentoProductoService {
    private final WebClient magentoWebClient;
    private static String  authToken;

    public MagentoProductoService(WebClient magentoWebClient) {
        this.magentoWebClient = magentoWebClient;
    }

    /**
     * Autenticacion de usuario, obtener token
     * @return token
     */
    public Mono<MagentoAuthToken> autenticarUsuario(){
        log.info("Magento: autenticacion del usuario... obteniendo token");
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
     * @param drupalProducto
     */
    public Mono<MagentoProductResponse> insertarProducto(DrupalProducto drupalProducto) {
        //comprobar si el token es nulo
        if(authToken == null){
            throw new AuthenticationFailedException("Magento: el token es nulo, usuario y contraseña no auntenticado", HttpStatus.BAD_REQUEST);
        }
        //mapear el producto de tipo drupal a magento
        MagentoProducto magentoProducto = mapDrupalProductoDetalleToMagento(drupalProducto);
        //enviar peticion HTTP a la API Magento
        log.info("Magento: enviando peticion a la API Magento del producto {}", drupalProducto.getSku());
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
}
