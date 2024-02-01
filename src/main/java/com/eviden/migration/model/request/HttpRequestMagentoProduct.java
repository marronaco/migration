package com.eviden.migration.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpRequestMagentoProduct {

    //atributos de la respuesta
    @JsonProperty("product")
    private Product product;

    //Inner clases de los atributos de tipo objeto
    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        @JsonProperty("sku")
        private String sku;

        @JsonProperty("name")
        private String name;

        @JsonProperty("attribute_set_id")
        private int attribute_set_id;

        @JsonProperty("price")
        private double price;

        @JsonProperty("type_id")
        private String type_id;

        @JsonProperty("weight")
        private double weight;

        @JsonProperty("custom_attributes")
        private List<Custom_attributes> customAttributes;
    }

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Custom_attributes {
        @JsonProperty("attribute_code")
        private String attributeCode;
        @JsonProperty("value")
        private String value;
    }
}
