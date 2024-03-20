package com.eviden.migration.utils;

import com.eviden.migration.models.drupal.DrupalProducto;
import com.eviden.migration.models.magento.MagentoProducto;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.eviden.migration.models.magento.MagentoProducto.*;

@Slf4j
public class MagentoProductMapper {
    private static final Map<String, String> MAP_CATEGORIA_TO_ID = new HashMap<>();
    private static final Map<String, String> MAP_DIFICULTAD_TO_ID = new HashMap<>();
    private static final Map<String, String> MAP_EDAD_TO_ID = new HashMap<>();
    private static final Map<String, String> MAP_JUGADORES_TO_ID = new HashMap<>();

    static {
        //Establecer las categorias segun clave (nombre en Drupal) - valor (ID en magento)
        MAP_CATEGORIA_TO_ID.put("Juegos de Mesa", "3");
        MAP_CATEGORIA_TO_ID.put("Oferta", "4");
        MAP_CATEGORIA_TO_ID.put("libros", "5");
        MAP_CATEGORIA_TO_ID.put("Accesorios", "10");
        MAP_CATEGORIA_TO_ID.put("Fundas", "11");
        MAP_CATEGORIA_TO_ID.put("Inserto", "12");
        MAP_CATEGORIA_TO_ID.put("PREventa", "14");
        MAP_CATEGORIA_TO_ID.put("Xtreme Art", "16");
        MAP_CATEGORIA_TO_ID.put("Actuales", "17");

        //Establecer las dificultad segun clave (nombre en Drupal) - valor (ID en magento)
        MAP_DIFICULTAD_TO_ID.put("1", "4");
        MAP_DIFICULTAD_TO_ID.put("2", "5");
        MAP_DIFICULTAD_TO_ID.put("3", "6");
        MAP_DIFICULTAD_TO_ID.put("4", "7");
        MAP_DIFICULTAD_TO_ID.put("5", "8");
        MAP_DIFICULTAD_TO_ID.put("6", "9");
        MAP_DIFICULTAD_TO_ID.put("7", "10");
        MAP_DIFICULTAD_TO_ID.put("8", "11");
        MAP_DIFICULTAD_TO_ID.put("9", "12");
        MAP_DIFICULTAD_TO_ID.put("10", "13");

        //Establecer las edad segun clave (edad en Drupal) - valor (ID en magento)
        MAP_EDAD_TO_ID.put("+18 años","63");
        MAP_EDAD_TO_ID.put("+16 años","64");
        MAP_EDAD_TO_ID.put("+14 años","62");
        MAP_EDAD_TO_ID.put("+13 años","65");
        MAP_EDAD_TO_ID.put("+12 años","61");
        MAP_EDAD_TO_ID.put("+11 años","66");
        MAP_EDAD_TO_ID.put("+10 años","60");
        MAP_EDAD_TO_ID.put("+9 años","67");
        MAP_EDAD_TO_ID.put("+8 años","59");
        MAP_EDAD_TO_ID.put("+7 años","68");
        MAP_EDAD_TO_ID.put("+6 años","69");
        MAP_EDAD_TO_ID.put("+5 años","70");
        MAP_EDAD_TO_ID.put("+4 años","71");
        MAP_EDAD_TO_ID.put("+3 años","72");
        MAP_EDAD_TO_ID.put("+2 años","73");

        //Establecer los jugadores segun clave (jugadores en Drupal) - valor (ID en magento)
        MAP_JUGADORES_TO_ID.put("1","14");
        MAP_JUGADORES_TO_ID.put("1-2","15");
        MAP_JUGADORES_TO_ID.put("1-4","16");
        MAP_JUGADORES_TO_ID.put("1-5","17");
        MAP_JUGADORES_TO_ID.put("1-6","18");
        MAP_JUGADORES_TO_ID.put("1-8","19");
        MAP_JUGADORES_TO_ID.put("2","20");
        MAP_JUGADORES_TO_ID.put("2-10","27");
        MAP_JUGADORES_TO_ID.put("2-12","28");
        MAP_JUGADORES_TO_ID.put("2-13","29");
        MAP_JUGADORES_TO_ID.put("2-3","21");
        MAP_JUGADORES_TO_ID.put("2-4","22");
        MAP_JUGADORES_TO_ID.put("2-5","23");
        MAP_JUGADORES_TO_ID.put("2-6","24");
        MAP_JUGADORES_TO_ID.put("2-7","25");
        MAP_JUGADORES_TO_ID.put("2-8","26");
        MAP_JUGADORES_TO_ID.put("3-4","30");
        MAP_JUGADORES_TO_ID.put("3-5","31");
        MAP_JUGADORES_TO_ID.put("3-6","32");
        MAP_JUGADORES_TO_ID.put("3-7","33");
        MAP_JUGADORES_TO_ID.put("3-8","34");
        MAP_JUGADORES_TO_ID.put("3-9","35");
        MAP_JUGADORES_TO_ID.put("3-10","36");
        MAP_JUGADORES_TO_ID.put("3-12","37");
        MAP_JUGADORES_TO_ID.put("4","8");
        MAP_JUGADORES_TO_ID.put("4-6","39");
        MAP_JUGADORES_TO_ID.put("4-7","40");
        MAP_JUGADORES_TO_ID.put("4-8","41");
        MAP_JUGADORES_TO_ID.put("4-9","42");
        MAP_JUGADORES_TO_ID.put("4-10","43");
        MAP_JUGADORES_TO_ID.put("4-12","44");
        MAP_JUGADORES_TO_ID.put("4-50","45");
        MAP_JUGADORES_TO_ID.put("5-6","46");
        MAP_JUGADORES_TO_ID.put("5-7","47");
        MAP_JUGADORES_TO_ID.put("5-10","48");
        MAP_JUGADORES_TO_ID.put("5-12","49");
        MAP_JUGADORES_TO_ID.put("6-10","50");
        MAP_JUGADORES_TO_ID.put("6-12","51");
        MAP_JUGADORES_TO_ID.put("6-15","52");
        MAP_JUGADORES_TO_ID.put("6-24","53");
        MAP_JUGADORES_TO_ID.put("8-18","54");
    }

    /**
     * Metodo para mapear un producto del drupal a magento
     * @param productDetail
     * @return
     */
    public static MagentoProducto mapDrupalProductoDetalleToMagento(DrupalProducto productDetail){
        log.info("Mapper: iniciando mapeo del producto: {}", productDetail.getSku());
        return builder()
                .product(mapProductoToMagento(productDetail))
                .build();
    }

    /**
     * Metodo para mapear el producto
     * @param productDetail
     * @return producto
     */
    private static Product mapProductoToMagento(DrupalProducto productDetail) {
        //mapeo del producto
        return Product.builder()
                .sku(productDetail.getSku())
                .name(productDetail.getTitle())
                .price(Double.parseDouble(productDetail.getPrecioVentaSinIva()))
                .status(mapPublicadoToMagento(productDetail.getPublicado()))
                .type_id("simple")
                .attribute_set_id(4)
                .extensionAttributes(mapExtensionAttributeToMagento(productDetail.getNivel(),
                                                                    productDetail.getCategorias(),
                                                                    productDetail.getTipo()))
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
                        mapJugadoresToMagento("jugadores", productDetail.getJugadores()),
                        //mapeo de umbral
                        mapUmbralToMagento("umbral", productDetail.getUmbral())
                ))
                .build();
    }

    /**
     * netodo para mapear el umbral del producto
     * @param attributeCode
     * @param umbral
     * @return
     */
    private static Custom_attributes mapUmbralToMagento(String attributeCode, String umbral) {
        log.info("Mapper: Umbral");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(umbral)
                .build();
    }

    /**
     * Metodo para mapear el stock del producto y las categorias
     * @param nivel
     * @param categorias
     * @param tipo
     * @return
     */
    private static ExtensionAttribute mapExtensionAttributeToMagento(String nivel,
                                                                     List<String> categorias,
                                                                     String tipo) {
        //Mapear catalag de drupal a magento producto
        List<CategoryLink> categoryLinks = mapCategoriasToMagento(categorias);
        //Mapear tipo de drupal a magento producto
        categoryLinks.add(mapCategoriaToMagento(tipo));
        return ExtensionAttribute.builder()
                .stock_item( mapNivelToMagento(nivel))
                .categoryLinks(categoryLinks)
                .build();
    }

    /**
     * Metodo para mapear la lista de categorias del producto
     * @param categorias
     * @return
     */
    private static List<CategoryLink> mapCategoriasToMagento(List<String> categorias) {
        log.info("Mapper: Categorias");
        return categorias.stream()
                .map(categoria -> mapCategoriaToMagento(categoria))
                .collect(Collectors.toList());
    }

    /**
     * Metodo para mapear la categoria del producto
     * @param categoria
     * @return
     */
    private static CategoryLink mapCategoriaToMagento(String categoria){
        //Mapear la categoria drupal a la categoria segun su ID en Magento
        //en caso de no coincidir con la categoria del MAP_CATEGORIA_TO_ID se colocara a la categoria 'No especificada' ID 18
        String categoriaID = MAP_CATEGORIA_TO_ID.getOrDefault(categoria,"18");
        return CategoryLink.builder()
                .position(0)
                .categoryId(categoriaID)
                .build();
    }

    /**
     * Metodo para mapear la oferta del producto
     * @param attributeCode
     * @param oferta
     * @return
     */
    private static Custom_attributes mapOfertaToMagento(String attributeCode, String oferta) {
        log.info("Mapper: Oferta");
        //En magento oferta: 1 = SI | 0 = NO
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(oferta.equals("Si") ? "1" : "0")
                .build();
    }

    /**
     * Metodo para mapear la visibilidad del producto
     * @param publicado
     * @return
     */
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
        //Mapear la categoria drupal a la categoria segun su ID en Magento
        String jugadoresID = MAP_JUGADORES_TO_ID.getOrDefault(jugadores,"75");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(jugadoresID)
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
        //Mapear la categoria drupal a la categoria segun su ID en Magento
        String difultadID = MAP_DIFICULTAD_TO_ID.getOrDefault(dificultad,"74");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(difultadID)
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
        //Mapear la categoria drupal a la categoria segun su ID en Magento
        String edadID = MAP_EDAD_TO_ID.getOrDefault(edad,"73");
        return   Custom_attributes.builder()
                .attributeCode(attributeCode)
                .value(edadID)
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
