package com.eviden.migration.models.magento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Esta clase define los atributos
 * que se enviaran al cuerpo de la solicitud para
 * la creacion del producto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagentoProducto {

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

        @JsonProperty("status")
        private double status; // 1 = Si | 2 = NO

        @JsonProperty("extension_attributes")
        private ExtensionAttribute extensionAttributes;

        @JsonProperty("custom_attributes")
        private List<Custom_attributes> customAttributes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Custom_attributes {
        @JsonProperty("attribute_code")
        private String attributeCode;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExtensionAttribute {
        @JsonProperty("stock_item")
        private StockItem stock_item;
        @JsonProperty("category_links")
        private List<CategoryLink> categoryLinks;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StockItem {
        @JsonProperty("qty")
        private int qty;
        @JsonProperty("is_in_stock")
        private boolean is_in_stock;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CategoryLink {
        @JsonProperty("position")
        private int position;
        @JsonProperty("category_id")
        private String categoryId;
    }
}
