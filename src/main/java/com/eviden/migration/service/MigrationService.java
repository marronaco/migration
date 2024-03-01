package com.eviden.migration.service;

import com.eviden.migration.model.DrupalProductoCsv;
import com.eviden.migration.model.DrupalUsuarioCsv;
import com.eviden.migration.model.request.MagentoUsuario;
import com.eviden.migration.model.response.MagentoAuthToken;
import com.eviden.migration.model.response.MagentoMediaResponse;
import com.eviden.migration.model.response.MagentoProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class MigrationService {
    private final DrupalProductoServiceCsv drupalProductoServiceCsv;
    private final DrupalUsuarioServiceCsv drupalUsuarioServiceCsv;
    private  final MagentoService magentoService;

    public MigrationService(DrupalProductoServiceCsv drupalProductoServiceCsv,
                            DrupalUsuarioServiceCsv drupalUsuarioServiceCsv,
                            MagentoService magentoService) {
        this.drupalProductoServiceCsv = drupalProductoServiceCsv;
        this.drupalUsuarioServiceCsv = drupalUsuarioServiceCsv;
        this.magentoService = magentoService;
    }

    //migrar producto de drupal a magento
    public void migracionProducto(){
        //drupal: obtener producto
        List<DrupalProductoCsv> drupalProductos = obtenerProdutosDrupal();

        //magento: autenticar usuario
        autenticarUsuarioMagento().block();
        //drupal: iterar sobre los productos
//        Flux.fromIterable(drupalProductos)
//                        .flatMapSequential(drupalProducto -> {
//                            return Mono.delay(Duration.ofSeconds(60))
//                                    .then(insertarProducto(drupalProducto))
//                                    .delayElement(Duration.ofSeconds(60))
//                                    .thenMany(insertarImagenesEnProducto(
//                                            drupalProducto.getImagesPath(),
//                                            drupalProducto.getSku()));
//                        })
//        .subscribe();
        // SOLO PRODUCTOS INSERTAR
//        Flux.fromIterable(drupalProductos)
//                .flatMapSequential(drupalProducto -> {
//                    return Mono.delay(Duration.ofSeconds(40))
//                            .then(insertarProducto(drupalProducto))
//                            .delayElement(Duration.ofSeconds(40));
//                })
//                .subscribe();
        // SOLO IMAGENES INSERTAR
        Flux.fromIterable(drupalProductos)
                        .flatMapSequential(drupalProducto -> {
                            return Mono.delay(Duration.ofSeconds(100))
                                    .thenMany(insertarImagenesEnProducto(
                                            drupalProducto.getImagesPath(),
                                            drupalProducto.getSku()));
                        })
        .subscribe();

    }

    private List<DrupalProductoCsv> obtenerProdutosDrupal() {
        return drupalProductoServiceCsv.importarProductosDrupalDesdeCsv();
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

//        return Flux.fromIterable(imagesPath)
//                .flatMapSequential(imagePath -> insertarImagenEnProductoConDelay(imagePath, productoSku));
        return Flux.fromIterable(imagesPath)
                        .flatMapSequential(imagePath -> {
                            return Mono.delay(Duration.ofSeconds(100))
                                    .then(insertarImagenEnProductoConDelay(imagePath, productoSku));
                        });
    }

    private Mono<MagentoMediaResponse> insertarImagenEnProductoConDelay(String imagePath, String productoSku){
        // Insertar imagen en el producto creado con un retraso de 2 segundos entre inserciones
        return insertarImagenEnProducto(imagePath, productoSku)
                .delayElement(Duration.ofSeconds(100));
    }

    private Mono<MagentoMediaResponse> insertarImagenEnProducto(String imagePath, String productoSku){
        //insertar imagen en el producto creado
        return  magentoService
                .insertarImagenEnProducto(imagePath, productoSku)
                .doOnSuccess(imagenResponse -> {
                    log.info("Magento: Imagen Response {}", imagenResponse.getImageId());
                });
    }

    public void migracionUsuario() {
        //magento: autenticar usuario
        autenticarUsuarioMagento().block();
        //drupal: obtener usuarios
        List<DrupalUsuarioCsv> drupalUsuarios = obtenerUsuarioDrupal();
        //drupal: iterar sobre los productos
        Flux.fromIterable(drupalUsuarios)
                .flatMapSequential(drupalUsuario -> {
                    return insertarUsuario(drupalUsuario);
                })
                .subscribe();


    }

    private List<DrupalUsuarioCsv> obtenerUsuarioDrupal() {
        return drupalUsuarioServiceCsv.importarUsuariosDrupalDesdeCsv();
    }

    private Mono<MagentoUsuario> insertarUsuario(DrupalUsuarioCsv usuarioCsv){
        //insertar producto en magento
        return magentoService.insertarUsuario(usuarioCsv)
                .doOnSuccess(magentoProducto -> {
                    log.info("Magento: Usuario Response {}", magentoProducto.getCustomer().getFirstname());
                });
    }
}
