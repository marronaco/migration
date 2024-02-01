package com.eviden.migration.model.domain;

import javax.persistence.*;
import java.io.Serializable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
public class ProductoMagento implements Serializable {
    
    private String sku; // Palabra mediante a la que acceder al producto (suele ser igual al nombre).
    private String name;
    private String url_key; // URL para acceder al producto (deberia ser que el SKU).

    private float price; // Precio de venta del producto.
    private float cost; // Precio de compra del producto (lo que nos costó).

    private int visibility; // Valor que dictamina quien puede ver el dato (1-4).

    private String weight;

    private String width;
    private String height;

    private int qty; // Cantidad de productos en stock.
    private boolean is_in_stock; // Si está o no a la venta.

    private String base64_image; // Archivo de imagen en Base64.
    private String image_value; // Direccion en  memoria fisica del servidor donde esta la imagen.

    private String description; // Descripcion larguita.
    private String short_description; // Descripcion cortita.

    private String country_of_manufacturer; // Pais de creacion (2 letras).


    
}