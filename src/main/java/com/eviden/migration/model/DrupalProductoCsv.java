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
    private String title;

    private List<String> imagesPath;

    private String precioVenta;

    private String precioMostrado;

    private String cost;

    private String path;

    private String nid;
}
