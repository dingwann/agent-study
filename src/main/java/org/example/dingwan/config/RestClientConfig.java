package org.example.dingwan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .defaultHeader("Content-Type", "application/json")
                .requestInterceptor((request, body, execution) -> {
                    // 可以在这里统一加 Token 等
                    return execution.execute(request, body);
                })
                .build();
    }

}