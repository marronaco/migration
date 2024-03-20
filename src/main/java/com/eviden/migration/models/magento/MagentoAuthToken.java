package com.eviden.migration.models.magento;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class MagentoAuthToken {
    private String token;
}
