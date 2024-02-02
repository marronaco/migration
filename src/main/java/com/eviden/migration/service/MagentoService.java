package com.eviden.migration.service;

import com.eviden.migration.model.request.MagentoAuthRequest;
import com.eviden.migration.model.request.MagentoAuthToken;
import com.eviden.migration.model.request.MagentoProducto;
import com.eviden.migration.model.response.DrupalProducto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.eviden.migration.utils.MagentoProductMapper.mapDrupalProductoDetalleToMagento;
@Slf4j
@Service
public class MagentoService {
    private final WebClient magentoWebClient;
    private String authToken;

    public MagentoService(WebClient magentoWebClient) {
        this.magentoWebClient = magentoWebClient;
    }

    /**
     * Autenticacion de usuario, obtener token
     */
    public Mono<Void> autenticarUsuario(){
        log.info("Magento: autenticacion del usuario, obteniendo CSRF...");
        //definir credenciales del usuario y contraseña
        MagentoAuthRequest authRequest = MagentoAuthRequest.builder()
                .userName("admin")
                .password("admin123")
                .build();

        //almacenar el token devuelto de la solicitud
        return magentoWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/integration/admin/token").build())
                .header("Content-Type", "application/json")
                .body(Mono.just(authRequest), MagentoAuthRequest.class)
                .retrieve()
                .bodyToMono(MagentoAuthToken.class)
                .doOnSuccess(magentoAuthToken -> {
                    // Almacenar el token obtenido en la variable
                    authToken = magentoAuthToken.getToken();
                })
                .then();
    }

    /**
     * Metodo para insertar producto en magento
     * @param drupalProducto
     */
    public Mono<Void> insertarProducto(DrupalProducto drupalProducto) {
        log.info("Magento: insertando producto '{}'", drupalProducto.getTitle());
        //mapear el producto de tipo drupal a magento
        MagentoProducto magentoProducto = mapDrupalProductoDetalleToMagento(drupalProducto);
        //enviar peticion HTTP a la API Magento
        return magentoWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/products").build())
                .header("Content-Type", "application/json")
                .header("Authorization", authToken)
                .body(Mono.just(magentoProducto), MagentoProducto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .then();
    }
}
