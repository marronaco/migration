package com.eviden.migration.utils;

import com.eviden.migration.model.DrupalProductoCsv;
import com.eviden.migration.model.request.MagentoProducto;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.eviden.migration.model.request.MagentoProducto.*;

@Slf4j
public class MagentoProductMapper {

    /**
     * Metodo para mapear un producto del drupal a magento
     * @param productDetail
     * @return
     */
    public static MagentoProducto mapDrupalProductoDetalleToMagento(DrupalProductoCsv productDetail){
        log.info("Mapper: iniciando mapeo del producto: {}", productDetail.getTitle());
        return builder()
                .product(mapProductoToMagento(productDetail))
                .build();
    }

    /**
     * Metodo para mapear el producto
     * @param productDetail
     * @return producto
     */
    private static Product mapProductoToMagento(DrupalProductoCsv productDetail) {
        log.info("Mapper: creando el objeto producto magento...");
        //mapeo del producto
        return Product.builder()
                .sku(productDetail.getSku())
                .name(productDetail.getTitle())
                .price(Double.parseDouble(productDetail.getPrecioVentaSinIva()))
                .status(mapPublicadoToMagento(productDetail.getPublicado()))
                .type_id("simple")
                .attribute_set_id(4)
                .extensionAttributes(mapExtensionAttributeToMagento(productDetail.getNivel(), productDetail.getCategoria()))
                .customAttributes(List.of(
                        //mapeo de la descripcion
                        mapDescripcionToMagento("description", productDetail.getDescripcion()),
                        //mapeo del costo
                        mapCostoToMagento("cost", productDetail.getCost()),
                        //mapeo de la uri
                        mapUriToMagento("url_key", productDetail.getPath()),
                        //mapeo del precio mostrado
                        mapOldPriceToMagento("pvp", productDetail.getOldPrice()),
                        //mapeo de la estanteria
                        mapEstanteriaToMagento("estanteria", productDetail.getEstanteria()),
                        //mapeo de la edad
                        mapEdadToMagento("edad", productDetail.getEdad()),
                        //mapeo de la editorial
                        mapEditorialToMagento("editorial", productDetail.getEditorial()),
                        //mapeo de la duracion
                        mapDuracionToMagento("duracion", productDetail.getDuracion()),
                        //mapeo de la dificultad
                        mapDificultadToMagento("dificultad", productDetail.getDificultad()),
                        //mapeo de la oferta
                        mapOfertaToMagento("oferta", productDetail.getOferta()),
                        //mapeo de los jugadores
                        mapJugadoresToMagento("num_jugadores", productDetail.getJugadores()),
                        //mapeo de umbral
                        mapUmbralToMagento("umbral", productDetail.getUmbral())
                ))
                .build();
    }

    private static Custom_attributes mapUmbralToMagento(String attributeCode, String umbral) {
        log.info("Mapper: Umbral");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(umbral)
                .build();
    }

    private static ExtensionAttribute mapExtensionAttributeToMagento(String nivel, String categoria) {
        return ExtensionAttribute.builder()
                .stock_item( mapNivelToMagento(nivel))
                .categoryLinks(mapCategoriasToMagento(categoria))
                .build();
    }

    private static List<CategoryLink> mapCategoriasToMagento(String categoria) {
        log.info("Mapper: Categorias");
        //TODO: incluir el id de categorias de Magento y evaluar todas las respuestas de categoria de drupal switch
        return List.of(
                CategoryLink.builder()
                        .position(0)
                        .categoryId(categoria.equals("Juegos de Mesa") ? "3" : "0") //juegos de mesa se alamcena en el id 3
                        .build()
        );
    }

    private static Custom_attributes mapOfertaToMagento(String attributeCode, String oferta) {
        log.info("Mapper: Oferta");
        //En magento oferta: 1 = SI | 0 = NO
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(oferta.equals("Si") ? "1" : "0")
                .build();
    }

    private static double mapPublicadoToMagento(String publicado) {
        log.info("Mapper: publicado");
        //En magento producto status (visibilidad): 1 = visible | 2 = NO visible
        return publicado.equals("Sí") ? 1 : 2;
    }

    /**
     * Metodo para maperar numeros de jugadores del producto
     * @param attributeCode
     * @param jugadores
     * @return
     */
    private static Custom_attributes mapJugadoresToMagento(String attributeCode, String jugadores) {
        log.info("Mapper: jugadores");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(jugadores)
                .build();
    }

    /**
     * Metodo para maperar el stock del producto
     * @param attributeCode
     * @param nivel
     * @return
     */
    private static StockItem mapNivelToMagento(String nivel) {
        log.info("Mapper: nivel");
        //parsear el nivel a numero
        int qyt = Integer.parseInt(nivel);
        return   StockItem.builder()
                .qty(qyt)
                .is_in_stock(qyt > 0 ? true : false)
                .build();
    }

    /**
     * Metodo para maperar la dificultad del producto
     * @param attributeCode
     * @param dificultad
     * @return
     */
    private static Custom_attributes mapDificultadToMagento(String attributeCode, String dificultad) {
        log.info("Mapper: dificultad");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(dificultad)
                .build();
    }

    /**
     * Metodo para maperar la duracion del producto
     * @param attributeCode
     * @param duracion
     * @return
     */
    private static Custom_attributes mapDuracionToMagento(String attributeCode, String duracion) {
        log.info("Mapper: duracion");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(duracion)
                .build();
    }

    /**
     * Metodo para maperar la editorial del producto
     * @param attributeCode
     * @param editorial
     * @return
     */
    private static Custom_attributes mapEditorialToMagento(String attributeCode, String editorial) {
        log.info("Mapper: editorial");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(editorial)
                .build();
    }

    /**
     * Metodo para maperar la edad del producto
     * @param attributeCode
     * @param edad
     * @return
     */
    private static Custom_attributes mapEdadToMagento(String attributeCode, String edad) {
        log.info("Mapper: edad");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(edad)
                .build();
    }

    /**
     * Metodo para maperar la estanteria del producto
     * @param attributeCode
     * @param estanteria
     * @return
     */
    private static Custom_attributes mapEstanteriaToMagento(String attributeCode, String estanteria) {
        log.info("Mapper: estanteria");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(estanteria)
                .build();
    }

/**
     * Metodo para maperar special price del producto
     * @param attributeCode
     * @param precioMostrado
     * @return
     */
    private static Custom_attributes mapOldPriceToMagento(String attributeCode, String precioMostrado) {
        log.info("Mapper: special price");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(precioMostrado)
                .build();
    }

    /**
     * Metodo para maperar la URI del producto
     * @param attributeCode
     * @param path
     * @return URI
     */
    private static Custom_attributes mapUriToMagento(String attributeCode, String path) {
        log.info("Mapper: URI");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(path)
                .build();
    }

    /**
     * Metodo para mapear el costo del producto
     * @param attributeCode
     * @param cost
     * @return costo
     */
    private static Custom_attributes mapCostoToMagento(String attributeCode, String cost) {
        log.info("Mapper: costo");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(cost)
                .build();
    }

    /**
     * Metodo para mapear la descripcion del producto
     * @param attributeCode
     * @param value
     * @return descripción
     */
    private static Custom_attributes mapDescripcionToMagento(String attributeCode, String descripcion) {
        log.info("Mapper: descripción");
        //descripción del producto
        return Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(descripcion)
                .build();
    }


    /**
     * Metodo para extraer la uri
     * que se utilizara para el SEO del producto
     * @param url
     * @return uri
     */
    private static String extraerURI(String url){
        log.info("Mapper: obteniendo la uri");
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            //comprobar el path
            if(path != null && !path.isEmpty()){
                //almacenar el path por bloques sin incluir '/'
                String[] segmento = path.split("/");
                //devolver el ultimo bloque de la uri
                return segmento[segmento.length - 1];
            }
        } catch (URISyntaxException e) {
            log.error("Error metodo extraerUri: {}", e.getMessage());
        }
        return "";
    }

}
