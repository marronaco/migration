package com.eviden.migration.model.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class MagentoAuthToken {
    private String token;
}
