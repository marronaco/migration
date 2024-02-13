package com.eviden.migration.utils;

import com.eviden.migration.exceptions.ImageFailedException;
import com.eviden.migration.model.request.MagentoMedia;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Slf4j
public class MagentoMediaMapper {

     //Metodo para mapear una imagen de drupal a magento
    public static MagentoMedia mapDrupalMedia(String imagePath){
        return mapProductoImagenToMagento(imagePath);
    }

    private static MagentoMedia mapProductoImagenToMagento(String imagePath) {
        //separar la ruta de la imagen
        String[] splitImagePath = imagePath.split("/");
        //alamcenar el nombre del fichero
        String imageFileName = splitImagePath[splitImagePath.length - 1];

        log.info("Iniciando mapeo de la imagen {}", imageFileName);
        return MagentoMedia.builder()
                .entry(MagentoMedia.Entry.builder()
                        .mediaType("image")
                        .label("new_picture")
                        .types(List.of(
                                "image",
                                "small_image",
                                "thumbnail",
                                "swatch_image"
                        ))
                        .file(imageFileName)
                        .content(MagentoMedia.Content.builder()
                                .base64EncodedData(mapByteImagenToBase64(imagePath))
                                .type("image/jpeg")
                                .name(imageFileName)
                                .build())
                        .build())
                .build();
    }

    private static String mapByteImagenToBase64(String imagePath) {
        log.info("Convirtiendo imagen a base64");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(imagePath);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            InputStream inputStream = response.getEntity().getContent();
            byte[] imageBytes = inputStream.readAllBytes();
            //devuelve la imagen en base64 string
            return Base64Utils.encodeToString(imageBytes);
        } catch (ClientProtocolException e) {
            throw new ImageFailedException(e.getMessage());
        } catch (IOException e) {
            throw new ImageFailedException(e.getMessage());
        }
    }
}
