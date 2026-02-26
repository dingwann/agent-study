package org.example.dingwan.tool;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.dingwan.model.entity.JsonRootBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class TavilyTool {

    @Value("${tavily.api-key}")
    private String tavily_api_key;

    @Resource
    private RestClient restClient;

    @PostConstruct
    public void init() {
        restClient = restClient.mutate()
                .defaultHeader("Authorization", "Bearer " + tavily_api_key)
                .build();
    }

    public JsonRootBean search(String query) {
        return restClient.post()
                .uri("https://api.tavily.com/search")
                .body(Map.of("query", query,
                        "search_depth", "basic",
                        "include_answer", "advanced"))
                .retrieve()
                .toEntity(JsonRootBean.class)
                .getBody();
    }

}
