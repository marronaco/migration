package com.eviden.migration.models.drupal;

import lombok.*;

import java.util.List;

/**
 * En esta clase se definen los atributos
 * establecidos la columnas del CSV
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class DrupalProducto {
    //atributos CSV drupal productos
    private String sku;
    private String title;
    private String path;
    private String descripcion;
    private String estanteria;
    private List<String> imagesPath;
    private String cost;
    private String precioVentaSinIva;
    private String oldPrice;
    private String edad;
    private String editorial;
    private String duracion;
    private String dificultad;
    private String oferta;
    private String nivel;
    private String publicado;
    private String jugadores;
    private String umbral;
    private List<String> categorias;
    private String tipo;
    private String peso;
}