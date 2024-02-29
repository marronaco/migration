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
        String[] rolesArray = linea[1].split(",");
        return DrupalUsuarioCsv.builder()
                .uid(linea[0])
                .rol(Arrays.asList(rolesArray))
                .nickname(linea[2])
                .email(linea[3])
                .nombre(linea[4])
                .apellidos(linea[5])
                .direccion1(linea[6])
                .direccion2(linea[7])
                .codigoPostal(linea[8])
                .ciudad(linea[9])
                .telefono(linea[10])
                .build();
    }
}
