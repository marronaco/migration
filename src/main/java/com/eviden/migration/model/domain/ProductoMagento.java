package com.eviden.migration.model.domain;

import javax.persistence.*;
import java.io.Serializable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "productomagento")
public class ProductoMagento implements Serializable {
    
    private String sku; // Palabra mediante a la que acceder al producto (suele ser igual al nombre).
    private String name;
    private int attribute_set_id;
    private float price;
    private int status;
    private int visibility; // Valor que dictamina quien puede ver el dato (1-4).
    private String type_id;
    private String created_at;
    private String updated_at;
    private String weight;

    private int qty; // Cantidad de productos en stock.

    private String file; // Archivo de imagen en Base64.
    //private String image_value; // Direccion en  memoria fisica del servidor donde esta la imagen.

    private String short_description;

    private String country_of_manufacturer; // Pais de creacion (2 letras).


    
}