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
        String[] imagenesArray = linea[5].split(",");
        //devuelve la creacion del nuevo objeto
        return DrupalProductoCsv.builder()
                    .sku(linea[0])
                    .title(linea[1])
                    .path(linea[2])
                    .descripcion(linea[3])
                    .estanteria(linea[4])
                    .imagesPath(Arrays.asList(imagenesArray))
                    .cost(linea[6])
                    .precioVenta(linea[7])
                    .precioMostrado(linea[8])
                    .edad(linea[9])
                    .editorial(linea[10])
                    .duracion(linea[11])
                    .dificultad(linea[12])
                    .oferta(linea[13])
                    .nivel(linea[14])
                    .publicado(linea[15])
                    .jugadores(linea[16])
                    .build();
    }
}
