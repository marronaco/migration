package com.eviden.migration.service;

import com.eviden.migration.model.DrupalProductoCsv;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DrupalServiceCsv {
    public List<DrupalProductoCsv> importarProductosDrupalDesdeCsv() {
        List<DrupalProductoCsv> productos = new ArrayList<>();

        try {
            log.info("Drupal: Lectura del CSV Productos...");
            //Lectura del fichero csv
            CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/drupal_productos.csv"));
            String[] linea;
            //salto la primera linea
            csvReader.readNext();
            while ((linea = csvReader.readNext()) != null) {
                DrupalProductoCsv producto = mapToProductoDrupalCsv(linea);
                productos.add(producto);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return productos;
    }

    private  DrupalProductoCsv mapToProductoDrupalCsv(String[] linea) {
        log.info("Drupal: obtenido producto {}", linea[0]);
        //separar la ruta de images a una array
        String[] imagenesArray = linea[1].split(", ");

        return DrupalProductoCsv.builder()
                .title(linea[0])
                .imagesPath(Arrays.asList(imagenesArray))
                .precioVenta(linea[2])
                .precioMostrado(linea[3])
                .cost(linea[4])
                .path(linea[5])
                .nid(linea[6])
                .build();

    }
}
