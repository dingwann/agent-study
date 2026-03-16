package org.example.dingwan.tool;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.consts.ChatRequestParam;
import org.example.dingwan.model.dto.req.*;
import org.example.dingwan.model.dto.res.ChatResponseRoot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * @author wangcai
 * @date 2026/2/26
 */
@Slf4j
@Component
public class ChatClient {

    @Value("${xiaomi.api-key}")
    private String apiKey;

    @Resource
    private RestClient restClient;

    @PostConstruct
    public void init() {
        log.info("init ChatClient");
        restClient = restClient.mutate()
                .baseUrl("https://api.xiaomimimo.com/v1/chat/completions")
                .defaultHeader("api-key", apiKey)
                .build();
    }

    public ChatResponseRoot chat(String userMessage) {
        ChatParamsRequest chatParamsRequest = ChatParamsRequest.builder()
                .model(ChatRequestParam.MODEL)
                .max_completion_tokens(ChatRequestParam.MAX_TOKEN)
                .thinking(Thinking.builder().type("enabled").build())
                .temperature(0.3)
                .messages(List.of(UserMessage.builder().content(userMessage).build()))
                .build();
        return restClient.post()
                .body(chatParamsRequest)
                .retrieve()
                .toEntity(ChatResponseRoot.class)
                .getBody();
    }

    public ChatResponseRoot chat(String userMessage, Double temperature) {
        ChatParamsRequest chatParamsRequest = ChatParamsRequest.builder()
                .model(ChatRequestParam.MODEL)
                .max_completion_tokens(ChatRequestParam.MAX_TOKEN)
                .thinking(Thinking.builder().type("enabled").build())
                .temperature(temperature)
                .messages(List.of(UserMessage.builder().content(userMessage).build()))
                .build();
        return restClient.post()
                .body(chatParamsRequest)
                .retrieve()
                .toEntity(ChatResponseRoot.class)
                .getBody();
    }

    public ChatResponseRoot chat(String userMessage, String systemMessage) {
        UserMessage userPrompt = UserMessage.builder().content(userMessage).build();
        SystemMessage systemPrompt = SystemMessage.builder().content(systemMessage).build();
        ChatParamsRequest chatParamsRequest = ChatParamsRequest.builder()
                .model(ChatRequestParam.MODEL)
                .max_completion_tokens(ChatRequestParam.MAX_TOKEN)
                .thinking(Thinking.builder().type("enabled").build())
                .temperature(0.3)
                .messages(List.of(systemPrompt, userPrompt))
                .build();
        return restClient.post()
                .body(chatParamsRequest)
                .retrieve()
                .toEntity(ChatResponseRoot.class)
                .getBody();
    }

    public ChatResponseRoot chat(String userMessage, String systemMessage, Double temperature) {
        UserMessage userPrompt = UserMessage.builder().content(userMessage).build();
        SystemMessage systemPrompt = SystemMessage.builder().content(systemMessage).build();
        ChatParamsRequest chatParamsRequest = ChatParamsRequest.builder()
                .model(ChatRequestParam.MODEL)
                .max_completion_tokens(ChatRequestParam.MAX_TOKEN)
                .thinking(Thinking.builder().type("enabled").build())
                .temperature(temperature)
                .messages(List.of(systemPrompt, userPrompt))
                .build();
        return restClient.post()
                .body(chatParamsRequest)
                .retrieve()
                .toEntity(ChatResponseRoot.class)
                .getBody();
    }

    public ChatResponseRoot chat(List<Message> messages) {
        ChatParamsRequest chatParamsRequest = ChatParamsRequest.builder()
                .model(ChatRequestParam.MODEL)
                .max_completion_tokens(ChatRequestParam.MAX_TOKEN)
                .thinking(Thinking.builder().type("enabled").build())
                .temperature(0.3)
                .messages(List.of(messages))
                .build();
        return restClient.post()
                .body(chatParamsRequest)
                .retrieve()
                .toEntity(ChatResponseRoot.class)
                .getBody();
    }

    public ChatResponseRoot chat(List<Message> messages, Double temperature) {
        ChatParamsRequest chatParamsRequest = ChatParamsRequest.builder()
                .model(ChatRequestParam.MODEL)
                .max_completion_tokens(ChatRequestParam.MAX_TOKEN)
                .thinking(Thinking.builder().type("enabled").build())
                .temperature(temperature)
                .messages(List.of(messages))
                .build();
        return restClient.post()
                .body(chatParamsRequest)
                .retrieve()
                .toEntity(ChatResponseRoot.class)
                .getBody();
    }

}
