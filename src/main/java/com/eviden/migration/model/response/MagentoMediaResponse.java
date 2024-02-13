package com.eviden.migration.model.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class MagentoMediaResponse {
    private String imageId;
}
