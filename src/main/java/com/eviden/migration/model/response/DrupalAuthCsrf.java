package com.eviden.migration.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrupalAuthCsrf {
    @JsonProperty("token")
    private String token;
}
