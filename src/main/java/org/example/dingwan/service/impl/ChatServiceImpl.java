package org.example.dingwan.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.service.ChatService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {


    /**
     * 调用llm生成回答
     * @param prompt --> 用户提示词
     * @param systemPrompt --> 系统提示词
     * @return String
     */
    @Override
    public String generate(String prompt, String systemPrompt) {
        log.info("正在调用llm生成回答");
        try {
        } catch (Exception e) {
            log.error("调用大模型失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return "";
    }

}
