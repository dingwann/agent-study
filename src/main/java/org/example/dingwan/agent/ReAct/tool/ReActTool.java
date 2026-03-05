package org.example.dingwan.agent.ReAct.tool;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.annotation.AiParam;
import org.example.dingwan.annotation.AiTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Component
public class ReActTool {

    @Resource
    private RestClient  restClient;
    @Value("${google.serp-api-key}")
    private String API_KEY;
    private static final String BASE_URL = "/search";

    @PostConstruct
    public void init() {
        restClient = restClient.mutate()
                .baseUrl("https://serpapi.com")
                .build();
    }

    @AiTool(name = "search", description = "一个基于SerpApi的实战网页搜索引擎工具。它会智能地解析搜索结果，优先返回直接答案或知识图谱信息。")
    public String search(@AiParam("搜索工具的查询参数") String query) {
        log.info("\uD83D\uDD0D 在执行 [SerpApi] 网页搜索: {}", query);

        // 检查 API Key
        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            log.error("❌ SERPAPI_API_KEY 未在配置文件中配置");
            return "错误：SERPAPI_API_KEY 未在配置文件中配置。";
        }

        try {
            // 构建请求参数
            Map response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(BASE_URL)
                            .queryParam("engine", "google")
                            .queryParam("q", query)
                            .queryParam("api_key", API_KEY)
                            .queryParam("gl", "cn")    // 国家代码
                            .queryParam("hl", "zh-cn") // 语言代码
                            .build())
                    .retrieve()
                    .body(Map.class); // SerpApi 返回的是动态 JSON，用 Map 接收最灵活

            // 智能解析逻辑 (优先寻找最直接的答案)
            // 检查 answer_box_list (多个答案框)
            if (response != null && response.containsKey("answer_box_list") && response.get("answer_box_list") instanceof List<?> boxList) {
                List<String> answers = new ArrayList<>();
                for (Object item : boxList) {
                    if (item instanceof Map) {
                        Object answer = ((Map<?, ?>) item).get("answer");
                        if (answer != null) answers.add(answer.toString());
                    }
                }
                if (!answers.isEmpty()) {
                    return String.join("\n", answers);
                }
            }

            // 检查 answer_box (单个答案框)
            if (response != null && response.containsKey("answer_box")) {
                Object answerBoxObj = response.get("answer_box");
                if (answerBoxObj instanceof Map<?, ?> answerBox) {
                    if (answerBox.containsKey("answer")) {
                        return answerBox.get("answer").toString();
                    }
                }
            }

            // 检查 knowledge_graph (知识图谱)
            if (response != null && response.containsKey("knowledge_graph")) {
                Object kgObj = response.get("knowledge_graph");
                if (kgObj instanceof Map<?, ?> knowledgeGraph) {
                    if (knowledgeGraph.containsKey("description")) {
                        return knowledgeGraph.get("description").toString();
                    }
                }
            }

            // 检查 organic_results (常规搜索结果，取前 3 个)
            if (response != null && response.containsKey("organic_results") && response.get("organic_results") instanceof List<?> organicResults) {
                if (!organicResults.isEmpty()) {
                    List<String> snippets = new ArrayList<>();
                    // 只取前 3 个
                    int limit = Math.min(3, organicResults.size());
                    for (int i = 0; i < limit; i++) {
                        Object resObj = organicResults.get(i);
                        if (resObj instanceof Map<?, ?> res) {
                            String title = Objects.toString(res.get("title"), "");
                            String snippet = Objects.toString(res.get("snippet"), "");
                            snippets.add(String.format("[%d] %s\n%s", i + 1, title, snippet));
                        }
                    }
                    if (!snippets.isEmpty()) {
                        return String.join("\n\n", snippets);
                    }
                }
            }

            // 默认返回
            return String.format("对不起，没有找到关于 '%s' 的信息。", query);
        } catch (Exception e) {
            log.error("搜索时发生错误", e);
            return String.format("搜索时发生错误：%s", e.getMessage());
        }
    }

}
