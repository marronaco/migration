package com.eviden.migration.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpResponseDrupalProductDetail {

    //atributos de la respuesta
    @JsonProperty("vid")
    private String vid;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("title")
    private String title;
    @JsonProperty("cost")
    private String cost;
    @JsonProperty("price")
    private String price;
    @JsonProperty("body")
    private Body body;
    @JsonProperty("uc_product_image")
    private UcProductImage ucProductImage;
    @JsonProperty("path")
    private String path;

    //Inner clases de los atributos de tipo objeto
    public static class Body {
        @JsonProperty("und")
        private List<BodyUnd> und;
    }

    public static class BodyUnd {
        @JsonProperty("value")
        private String value;
    }

    public static class UcProductImage {
        @JsonProperty("und")
        private List<ImageInfo> imageInfo;
    }

    public static class ImageInfo {
        @JsonProperty("fid")
        private String fid;
        @JsonProperty("uid")
        private String uid;
        @JsonProperty("filename")
        private String filename;
        @JsonProperty("uri")
        private String  uri;
        @JsonProperty("filemime")
        private String filemime;
        @JsonProperty("filesize")
        private String filesize;
        @JsonProperty("status")
        private String status;
        @JsonProperty("timestamp")
        private String timestamp;
        @JsonProperty("width")
        private String width;
        @JsonProperty("height")
        private String  height;
    }
}
