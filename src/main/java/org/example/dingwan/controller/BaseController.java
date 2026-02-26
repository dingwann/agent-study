package org.example.dingwan.controller;

import jakarta.annotation.Resource;
import org.example.dingwan.common.BaseResponse;
import org.example.dingwan.common.ResultUtils;
import org.example.dingwan.consts.ChatRequestParam;
import org.example.dingwan.model.dto.req.ChatParamsRequest;
import org.example.dingwan.model.dto.req.UserMessage;
import org.example.dingwan.model.dto.res.ChatResponseRoot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/base")
public class BaseController {

    @Resource
    private RestClient restClient;

    @Value("${xiaomi.api-key}")
    private String apiKey;

    @GetMapping("/health")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("ok");
    }

    @GetMapping("/chat")
    public BaseResponse<ChatResponseRoot> chat(@RequestParam String query) {
        ChatParamsRequest paramsRequest = ChatParamsRequest.builder()
                .model(ChatRequestParam.MODEL)
                .messages(List.of(UserMessage.builder()
                        .content(query)
                        .build()))
                .max_completion_tokens(ChatRequestParam.MAX_TOKEN)
                .build();
        ChatResponseRoot string = restClient.post()
                .uri("https://api.xiaomimimo.com/v1/chat/completions")
                .header("api-key", apiKey)
                .body(paramsRequest)
                .retrieve()
                .toEntity(ChatResponseRoot.class)
                .getBody();
        return ResultUtils.success(string);
    }

}
