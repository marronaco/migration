package com.eviden.migration.model.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class MagentoAuthToken {
    private String token;
}
