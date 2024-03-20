package com.eviden.migration.service;

import com.eviden.migration.models.drupal.DrupalProducto;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que gestiona los productos de Drupal
 */
@Slf4j
@Service
public class DrupalProductoService {
    private static int contadorImagenesPath = 0;

    /**
     * Metodo para la lectura del CSV
     * y almacenarlos en una lista
     * @return Listado de productos
     */
    public List<DrupalProducto> importarProductosDrupalDesdeCsv() {
        List<DrupalProducto> productos = new ArrayList<>();

        try {
            //Lectura del fichero csv
            String rutaEscritorio = System.getenv("CSV_DIRECTORY");
            String rutaPath = Paths.get(rutaEscritorio,"productos.csv").toString();

            log.info("Drupal: Lectura del CSV '{}'", rutaPath);
            CSVReader csvReader = new CSVReader(new FileReader(rutaPath));
            String[] linea;
            //salto la primera linea
            csvReader.readNext();
            while ((linea = csvReader.readNext()) != null) {
                DrupalProducto producto = mapToProductoDrupalCsv(linea);
                productos.add(producto);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        log.info("Total de productos a insertar '{}'", productos.size());
        log.info("Total imagenes path a insertar '{}'", contadorImagenesPath);
        return productos;
    }

    private DrupalProducto mapToProductoDrupalCsv(String[] linea) {
        log.info("Drupal: producto sku '{}'", linea[0]);
        //separar la ruta de images a un array
        String[] imagenesArray = linea[5].split("\\s*,\\s*");
        //contador del listado de imagenes path
        contadorImagenesPath += imagenesArray.length;
        //separara la cadena de categoria a un array
        String[] categorias = linea[18].split("\\s*,\\s*");
        //devuelve la creacion del nuevo objeto
        return DrupalProducto.builder()
                    .sku(linea[0])
                    .title(linea[1])
                    .path(linea[2])
                    .descripcion(linea[3])
                    .estanteria(linea[4])
                    .imagesPath(List.of(imagenesArray))
                    .cost(linea[6])
                    .precioVentaSinIva(linea[7])
                    .oldPrice(linea[8])
                    .edad(linea[9])
                    .editorial(linea[10])
                    .duracion(linea[11])
                    .dificultad(linea[12])
                    .oferta(linea[13])
                    .nivel(linea[14])
                    .publicado(linea[15])
                    .jugadores(linea[16])
                    .umbral(linea[17])
                    .categorias(List.of(categorias))
                    .tipo(linea[19])
                    .peso(linea[20])
                    .build();
    }
}
