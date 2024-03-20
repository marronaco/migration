package com.eviden.migration.service;

import com.eviden.migration.models.drupal.DrupalProducto;
import com.eviden.migration.models.magento.MagentoAuthToken;
import com.eviden.migration.models.magento.MagentoProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class MigrationService {
    private final DrupalProductoService drupalProductoService;
    private  final MagentoProductoService magentoProductoService;

    public MigrationService(DrupalProductoService drupalProductoService,
                            MagentoProductoService magentoProductoService) {
        this.drupalProductoService = drupalProductoService;
        this.magentoProductoService = magentoProductoService;
    }

    //migrar producto de drupal a magento
    public void migracionProducto(){
        //drupal: obtener producto
        List<DrupalProducto> drupalProductos = obtenerProdutosDrupal();
        //magento: autenticar usuario
        autenticarUsuarioMagento().block();
        //drupal: iterar sobre los productos
        Flux.fromIterable(drupalProductos)
                .flatMapSequential(drupalProducto -> {
                    return Mono.delay(Duration.ofSeconds(100))
                            .then(insertarProducto(drupalProducto));
                })
                .subscribe(null,
                        error -> {}
                        ,() -> {
                    log.info("Migracion finalizada");
                    //cerrar el prorama
                    System.exit(0);
                });
    }

    private List<DrupalProducto> obtenerProdutosDrupal() {
        return drupalProductoService.importarProductosDrupalDesdeCsv();
    }

    private Mono<MagentoAuthToken> autenticarUsuarioMagento(){
        return magentoProductoService.autenticarUsuario();
    }

    private Mono<MagentoProductResponse> insertarProducto(DrupalProducto drupalProducto){
        //insertar producto en magento
        return magentoProductoService.insertarProducto(drupalProducto)
                .doOnSuccess(magentoProducto -> {
                    log.info("Magento: insertado el producto con sku {}", magentoProducto.getSku());
                });
    }
}
