package com.eviden.migration.model.testing;

import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.WebClient;

public class HttpTestResponse {

    @NonNull
    static String url = "https://catfact.ninja/fact?max_length=140";

    static WebClient.Builder testBuilder = WebClient.builder();

    public static TestEntity test = testBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(TestEntity.class)
                    .block();
}
