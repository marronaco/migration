package com.eviden.migration.service;

import com.eviden.migration.model.DrupalProductoCsv;
import com.eviden.migration.model.response.MagentoAuthToken;
import com.eviden.migration.model.response.MagentoMediaResponse;
import com.eviden.migration.model.response.MagentoProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class MigrationService {
    private final DrupalServiceCsv drupalServiceCsv;
    private  final MagentoService magentoService;

    public MigrationService(DrupalServiceCsv drupalServiceCsv, MagentoService magentoService) {
        this.drupalServiceCsv = drupalServiceCsv;
        this.magentoService = magentoService;
    }

    //migrar producto de drupal a magento
    public void migracionProducto(){
        //drupal: obtener producto
        List<DrupalProductoCsv> drupalProductos = obtenerProdutosDrupal();
        //magento: autenticar usuario
        autenticarUsuarioMagento().block();
        //drupal: iterar sobre los productos
        Flux.fromIterable(drupalProductos)
                        .flatMapSequential(drupalProducto -> {
                            return insertarProducto(drupalProducto)
                                    .thenMany(insertarImagenesEnProducto(
                                            drupalProducto.getImagesPath(),
                                            drupalProducto.getSku()));
                        })
        .subscribe();

    }

    private List<DrupalProductoCsv> obtenerProdutosDrupal() {
        return drupalServiceCsv.importarProductosDrupalDesdeCsv();
    }

    private Mono<MagentoAuthToken> autenticarUsuarioMagento(){
        return magentoService.autenticarUsuario();
    }

    private Mono<MagentoProductResponse> insertarProducto(DrupalProductoCsv drupalProducto){
        //insertar producto en magento
        return magentoService.insertarProducto(drupalProducto)
                .doOnSuccess(magentoProducto -> {
                    log.info("Magento: producto Response {}", magentoProducto.toString());
                });
    }

    private Flux<MagentoMediaResponse> insertarImagenesEnProducto(List<String> imagesPath, String productoSku){
        log.info("Cantidad de imagenes asociadas al producto: {}", imagesPath.size());
        //listado de rutas de images del producto
        return Flux.fromIterable(imagesPath)
                .flatMapSequential(imagePath -> insertarImagenEnProducto(imagePath, productoSku));
    }

    private Mono<MagentoMediaResponse> insertarImagenEnProducto(String imagePath, String productoSku){
        //insertar imagen en el producto creado
        return  magentoService
                .insertarImagenEnProducto(imagePath, productoSku)
                .doOnSuccess(imagenResponse -> {
                    log.info("Magento: Imagen Response {}", imagenResponse.getImageId());
                });
    }
}
