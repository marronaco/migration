package com.eviden.migration.utils;

import com.eviden.migration.model.request.HttpRequestMagentoProduct;
import com.eviden.migration.model.request.HttpRequestMagentoProduct.Custom_attributes;
import com.eviden.migration.model.response.HttpResponseDrupalProductDetail;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.eviden.migration.model.response.HttpResponseDrupalProductDetail.BodyUnd;

@Slf4j
public class MagentoProductMapper {

    /**
     * Metodo para mapear un producto del drupal a magento
     * @param productDetail
     * @return
     */
    public static HttpRequestMagentoProduct drupalProductDetailToMagentoProduct(HttpResponseDrupalProductDetail productDetail){
        log.info("Iniciando mapeo del producto: {}", productDetail.getTitle());
        return HttpRequestMagentoProduct.builder()
                .product(productoMapperToMagento(productDetail))
                .build();
    }

    /**
     * Metodo para mapear el producto
     * @param productDetail
     * @return producto
     */
    private static HttpRequestMagentoProduct.Product productoMapperToMagento(HttpResponseDrupalProductDetail productDetail) {
        log.info("Mapeo del producto detalle drupal a magento...");
        //descripción del producto
        Optional<BodyUnd> value = productDetail.getBody().getUnd()
                .stream()
                .findFirst();

        //mapeo del producto
        return HttpRequestMagentoProduct.Product.builder()
                .sku(productDetail.getTitle())
                .name(productDetail.getTitle())
                .price(Double.parseDouble(productDetail.getPrice()))
                .type_id("simple")
                .weight(Double.parseDouble(productDetail.getWeight()))
                .customAttributes(List.of(
                        //mapeo de la descripcion del producto
                        descripcionMapperToMagento("description", value),
                        //mapeo del costo del producto
                        costoMapperToMagento("cost", productDetail.getCost()),
                        //maepo de la uri del producto
                        uriMapperToMagento("url_key", productDetail.getPath())
                ))
                .build();
    }

    /**
     * Metodo para maperar la URI del producto
     * @param attributeCode
     * @param path
     * @return URI
     */
    private static Custom_attributes uriMapperToMagento(String attributeCode, String path) {
        log.info("Mapeo URI del producto...");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(extraerURI(path))
                .build();
    }

    /**
     * Metodo para mapear el costo del producto
     * @param attributeCode
     * @param cost
     * @return costo
     */
    private static Custom_attributes costoMapperToMagento(String attributeCode, String cost) {
        log.info("Mapeo costo del producto...");
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
    private static Custom_attributes descripcionMapperToMagento(String attributeCode, Optional<BodyUnd> value) {
        log.info("Mapeo descripción del producto...");
        return Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(String.valueOf(value
                        .orElse(BodyUnd.builder().value("").build())))
                .build();
    }


    /**
     * Metodo para extraer la uri
     * que se utilizara para el SEO del producto
     * @param url
     * @return uri
     */
    private static String extraerURI(String url){
        log.info("Obteniendo la uri del producto..");
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
