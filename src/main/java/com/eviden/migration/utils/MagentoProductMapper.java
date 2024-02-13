package com.eviden.migration.utils;

import com.eviden.migration.model.DrupalProductoCsv;
import com.eviden.migration.model.request.MagentoProducto;
import com.eviden.migration.model.request.MagentoProducto.Custom_attributes;
import com.eviden.migration.model.response.DrupalProductoJson;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.eviden.migration.model.request.MagentoProducto.*;
import static com.eviden.migration.model.response.DrupalProductoJson.BodyUnd;

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
                //TODO: cambiar sku por el sku del productDetail
                .sku(productDetail.getTitle())
                .name(productDetail.getTitle())
                .price(Double.parseDouble(productDetail.getPrecioVenta()))
                .type_id("simple")
                .attribute_set_id(4) //este atributo posiblemente es en el que se establece las categorias
                .customAttributes(List.of(
                        //mapeo de la descripcion del producto
                       // mapDescripcionToMagento("description", productDetail.getBody().getUnd()),
                        //mapeo del costo del producto
                        mapCostoToMagento("cost", productDetail.getCost()),
                        //maepo de la uri del producto
                        mapUriToMagento("url_key", productDetail.getPath()),
                        //maepo del precio mostrado del producto
                        mapSpecialPriceToMagento("special_price", productDetail.getPrecioMostrado())
                ))
                .build();
    }

    private static Custom_attributes mapSpecialPriceToMagento(String attributeCode, String precioMostrado) {
        log.info("Mapper: special price del producto");
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
        log.info("Mapper: URI del producto");
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
        log.info("Mapper: costo del producto");
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
    private static Custom_attributes mapDescripcionToMagento(String attributeCode, List<BodyUnd> value) {
        log.info("Mapper: descripción del producto");
        //descripción del producto
        String valueFound = value.stream()
                .findFirst()
                .map(BodyUnd::getValue)
                .orElse("");

        return Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(valueFound)
                .build();
    }


    /**
     * Metodo para extraer la uri
     * que se utilizara para el SEO del producto
     * @param url
     * @return uri
     */
    private static String extraerURI(String url){
        log.info("Mapper: obteniendo la uri del producto");
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
