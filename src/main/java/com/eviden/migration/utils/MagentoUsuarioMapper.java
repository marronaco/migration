package com.eviden.migration.utils;

import com.eviden.migration.model.DrupalUsuarioCsv;
import com.eviden.migration.model.request.MagentoUsuario;
import com.eviden.migration.model.request.MagentoUsuario.Addresses;
import com.eviden.migration.model.request.MagentoUsuario.ExtensionAttributes;
import com.eviden.migration.model.request.MagentoUsuario.Region;
import lombok.extern.slf4j.Slf4j;

import static java.util.List.of;

@Slf4j
public class MagentoUsuarioMapper {

    public static MagentoUsuario mapDrupalUsuarioToMagento(DrupalUsuarioCsv usuario){
        log.info("Mapper: iniciando mapeo del usuario: {}", usuario.getNombre());
        return  MagentoUsuario.builder()
                .groupId(4)
                .email(usuario.getEmail())
                .firstname(usuario.getNombre())
                .lastname(usuario.getApellidos())
                .gender(0) //TODO: revisar los ids de gender
                .storeId(1)
                .websiteId(1)
                .addresses(of(mapAddressesToMagento(usuario.getDireccion1(), usuario.getDireccion2(),
                                                 usuario.getTelefono(), usuario.getCodigoPostal(),
                                                 usuario.getCiudad(), usuario.getNombre(),
                                                 usuario.getApellidos())))
                .disableAutoGroupChange(0)
                .extensionAttributes(mapExtensionAttributesToMagento())
                .password("1234") //TODO: preguntar jesus por la pass por defecto
                .build();
    }

    private static Addresses mapAddressesToMagento(String direccion1, String direccion2,
                                                   String telefono, String codigoPostal,
                                                   String ciudad, String nombre,
                                                   String apellidos) {
        log.info("Mapper: Adresses");
        return Addresses.builder()
                .id(1)
                .customerId(2) //TODO: revisar si es por default
                .region(mapRegionToMagento()) //TODO: revisar si del csv podemos saber la comunidad y region code
                .regionId(0)
                .countryId("ES")
                .street(of(direccion1, direccion2))
                .telephone(telefono)
                .postcode(codigoPostal)
                .city(ciudad)
                .firstname(nombre)
                .lastname(apellidos)
                .defaultShipping(true)
                .defaultBilling(true)
                .build();

    }

    private static Region mapRegionToMagento() {
        log.info("Mapper: Region");
        return null;
    }

    private static ExtensionAttributes mapExtensionAttributesToMagento() {
        log.info("Mapper: Extension attributes");
        return ExtensionAttributes.builder()
                .isSubscribed(false)
                .build();
    }

}
