package com.eviden.migration.utils;

import com.eviden.migration.model.DrupalUsuarioCsv;
import com.eviden.migration.model.request.MagentoUsuario;
import com.eviden.migration.model.request.MagentoUsuario.Addresses;
import com.eviden.migration.model.request.MagentoUsuario.Customer;
import com.eviden.migration.model.request.MagentoUsuario.ExtensionAttributes;
import com.eviden.migration.model.request.MagentoUsuario.Region;
import lombok.extern.slf4j.Slf4j;

import static java.util.List.of;

@Slf4j
public class MagentoUsuarioMapper {

    public static MagentoUsuario mapDrupalUsuarioToMagento(DrupalUsuarioCsv usuario){
        log.info("Mapper: iniciando mapeo del usuario: {}", usuario.getNombre());
        return  MagentoUsuario.builder()
                .customer(mapCustomerToMagento(usuario))
                .password("Eviden2024") //TODO: preguntar jesus por la pass por defecto
                .build();
    }

    private static Customer mapCustomerToMagento(DrupalUsuarioCsv usuario) {
        return Customer.builder()
                .groupId(4) //TODO: MAPEAR ROL
                .email(usuario.getEmail())
                .firstname(usuario.getNombre())
                .lastname(usuario.getApellidos())
                .gender(3) //ID 3: NOT SPECIFIED
                .storeId(1)
                .websiteId(1)
                .addresses(of(mapAddressesToMagento(usuario.getDireccion1(), usuario.getDireccion2(),
                        usuario.getTelefono(), usuario.getCodigoPostal(),
                        usuario.getCiudad(), usuario.getNombre(),
                        usuario.getApellidos(), usuario.getProvincia(),
                        usuario.getCodigoProvincia())))
                .disableAutoGroupChange(0)
                .extensionAttributes(mapExtensionAttributesToMagento())
                .build();
    }

    private static Addresses mapAddressesToMagento(String direccion1, String direccion2,
                                                   String telefono, String codigoPostal,
                                                   String ciudad, String nombre,
                                                   String apellidos, String provincia,
                                                   String codProvincia) {
        log.info("Mapper: Adresses");
        return Addresses.builder()
                .id(1)
                .customerId(2) //TODO: revisar si es por default
                .region(mapRegionToMagento(codProvincia, provincia))
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

    private static Region mapRegionToMagento(String codProvincia, String provincia) {
        log.info("Mapper: Region");
        return Region.builder()
                .regionCode(codProvincia)
                .region(provincia)
                .regionId(0)
                .build();
    }

    private static ExtensionAttributes mapExtensionAttributesToMagento() {
        log.info("Mapper: Extension attributes");
        return ExtensionAttributes.builder()
                .isSubscribed(false)
                .build();
    }

}
