package com.eviden.migration.model;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class DrupalUsuarioCsv {
    //atributos del CSV
    private String uid;
    private List<String> rol;
    private String nickname;
    private String email;
    private String nombre;
    private String apellidos;
    private String direccion1;
    private String direccion2;
    private String codigoPostal;
    private String ciudad;
    private String telefono;
}
