package com.eviden.migration.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrupalProducto {

    //atributos de la respuesta
    @JsonProperty("vid")
    private String vid;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("title")
    private String title;

    @JsonProperty("cost")
    private String cost;

    @JsonProperty("weight")
    private String weight;

    @JsonProperty("price")
    private String price;

    @JsonProperty("body")
    private Body body;

    @JsonProperty("uc_product_image")
    private UcProductImage ucProductImage;

    @JsonProperty("path")
    private String path;

    //Inner clases de los atributos de tipo objeto
    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        @JsonProperty("und")
        private List<BodyUnd> und;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BodyUnd {
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UcProductImage {
        @JsonProperty("und")
        private List<ImageInfo> imageInfo;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageInfo {
        @JsonProperty("filename")
        private String filename;
        @JsonProperty("uri")
        private String  uri;
        @JsonProperty("filemime")
        private String filemime;
//        @JsonProperty("fid")
//        private String fid;
//        @JsonProperty("uid")
//        private String uid;
//        @JsonProperty("filesize")
//        private String filesize;
//        @JsonProperty("status")
//        private String status;
//        @JsonProperty("timestamp")
//        private String timestamp;
//        @JsonProperty("width")
//        private String width;
//        @JsonProperty("height")
//        private String  height;
    }
}
