# MIGRACIÓN PRODUCTOS

La migración de datos se basa en un archivo CSV estructurado que contiene información detallada sobre 
los productos a transferir. Este archivo, denominado ``productos.csv``, sigue un formato específico para 
una interpretación correcta de los datos. La aplicación procesará este archivo 
y realizará la inserción de los productos en Magento 2 a través de un servicio REST de su API.

***

## Tecnologías Utilizadas

- Java 17
- Spring Boot v2.7.18
- Lombok para reducir el código boilerplate
- WebFlux para la programación reactiva
- OpenCSV para la lectura y escritura de archivos CSV

