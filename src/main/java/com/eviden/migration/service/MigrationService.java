package com.eviden.migration.service;

import com.eviden.migration.model.response.DrupalAuthCsrf;
import com.eviden.migration.model.response.DrupalProducto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
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
    public Mono<Void> migracionProducto(){
        //obtener producto en drupal
        Mono<DrupalAuthCsrf> drupalCSRF = drupalService.obtenerDrupalCSRF()
                .doOnSuccess(authCsrf -> {
                    log.info("CSRF: {}", authCsrf.getToken());
                })
                .doOnError(error -> {
                    // Manejar errores, imprimir en el log o realizar acciones específicas
                    log.error("Error during migration: {}", error.getMessage(), error);
                });
        Mono<DrupalProducto> drupalProducto = drupalService.obtenerProductoByNid(2)
                .doOnSuccess(producto -> {
                    log.info("Producto {}", producto);
                })
                .doOnError(error -> {
                    log.error("Error during migration: {}", error.getMessage(), error);
                });

        drupalCSRF.subscribe();
        drupalProducto.subscribe();

        return drupalProducto.then();
//        //autenticar usuario en magento
//        Mono<Void> magentoAuthMono = magentoService.autenticarUsuario();
//
//        return Mono.zip(drupalProductoMono, magentoAuthMono)
//                .flatMap(tuple -> {
//                    DrupalProducto drupalProducto = tuple.getT1();
//
//                    return magentoService.insertarProducto(drupalProducto);
//                })
//                .then();
    }
}
