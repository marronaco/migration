package com.eviden.migration.service;

import com.eviden.migration.model.DrupalUsuarioCsv;
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
public class DrupalUsuarioServiceCsv {
    public List<DrupalUsuarioCsv> importarUsuariosDrupalDesdeCsv() {
        List<DrupalUsuarioCsv> usuarios = new ArrayList<>();

        try {
            log.info("Drupal: Lectura del CSV Usuarios...");
            //Lectura del fichero csv
            CSVReader csvReader = new CSVReader(new FileReader("src/main/resources/users.csv"));
            String[] linea;
            //salto la primera linea
            csvReader.readNext();
            while ((linea = csvReader.readNext()) != null) {
                DrupalUsuarioCsv usuario = mapToUsuarioDrupalCsv(linea);
                usuarios.add(usuario);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        log.info("Total de usuarios a insertar {}", usuarios.size());
        return usuarios;
    }

    private DrupalUsuarioCsv mapToUsuarioDrupalCsv(String[] linea) {
        // Separar como con imagenes los roles por coma
        return DrupalUsuarioCsv.builder()
                .uid(linea[0])
                .rol(linea[1])
                .email(linea[2])
                .nombre(linea[3])
                .apellidos(linea[4])
                .direccion1(linea[5])
                .direccion2(linea[6])
                .codigoPostal(linea[7])
                .ciudad(linea[8])
                .provincia(linea[9])
                .codigoProvincia(linea[10])
                .telefono(linea[11])
                .build();
    }
}
