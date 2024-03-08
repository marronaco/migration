package com.eviden.migration.model;

import com.eviden.migration.model.response.DrupalProductoJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class DrupalProductoCsv {
    //atributos del CSV
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
}