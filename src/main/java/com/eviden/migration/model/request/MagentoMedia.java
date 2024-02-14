package com.eviden.migration.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagentoMedia {

    @JsonProperty("entry")
    private Entry entry;

    @Data
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entry {

        @JsonProperty("media_type")
        private String mediaType;

        @JsonProperty("label")
        private String label;

        @JsonProperty("disabled")
        private boolean disabled;

        @JsonProperty("types")
        private List<String> types;

        @JsonProperty("file")
        private String file;

        @JsonProperty("content")
        private Content content;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {

        @JsonProperty("base64_encoded_data")
        private String base64EncodedData;

        @JsonProperty("type")
        private String type;

        @JsonProperty("name")
        private String name;
    }
}
