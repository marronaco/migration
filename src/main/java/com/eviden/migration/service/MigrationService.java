package com.eviden.migration.service;

import com.eviden.migration.model.request.MagentoAuthToken;
import com.eviden.migration.model.response.DrupalProducto;
import com.eviden.migration.model.response.MagentoProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MigrationService {
    private final DrupalService drupalService;
    private  final MagentoService magentoService;

    public MigrationService(DrupalService drupalService, MagentoService magentoService) {
        this.drupalService = drupalService;
        this.magentoService = magentoService;
    }

    //migrar producto de drupal a magento
    public void migracionProducto(){
        //magento: autenticar usuario
        Mono<MagentoAuthToken> authMagento = magentoService.autenticarUsuario();
        //subscribir a la llamada
        authMagento.subscribe();

        //drupal:obtener producto en drupal
        Mono<DrupalProducto> drupalProducto = drupalService.obtenerDrupalCSRF()
                .doOnSuccess(authCsrf -> {
                    log.info("CSRF: {}", authCsrf.getToken());
                })
                .then(drupalService.obtenerProductoByNid(2)
                        .doOnSuccess(producto -> {
                            log.info("Drupal Producto {}", producto.toString());
                        })
                );
        //magento: insertar producto
        Mono<MagentoProductResponse> magentoResponse = magentoService.insertarProducto(drupalProducto.block())
                .doOnSuccess(magentoProducto -> {
                    log.info("Magento Producto {}", magentoProducto.toString());
                });
        //subscribir a la llamada
        magentoResponse.subscribe();
        
    }
}
