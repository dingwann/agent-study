package org.example.dingwan.agent.planandsolve;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.agent.prompt.PlanAndSolvePrompt;
import org.example.dingwan.model.dto.req.Message;
import org.example.dingwan.model.dto.req.UserMessage;
import org.example.dingwan.tool.ChatClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class Planner {

    private final ChatClient chatClient;

    public Planner(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 根据用户的问题生成一个计划列表
     * @param question 用户问题
     * @return 计划表
     */
    public List<String> plan(String question) {
        String plannerPromptTemplate = PlanAndSolvePrompt.PLANNER_PROMPT_TEMPLATE;
        String prompt = plannerPromptTemplate.replace("{question}", question);
        // 构建消息列表
        List<Message> messages = new ArrayList<>();
        messages.add(UserMessage.builder().content(prompt).build());
        log.info("--- 正在生成计划 ---");
        String content = this.chatClient.chat(messages).getChoices().get(0).getMessage().getContent();
        log.info("✔ 计划已生成：\n{}", content);
        // 解析LLM生成的计划表
        try {
            // 找到```Java和```之间的内容
            if (!content.contains("```java")) {
                return Collections.emptyList();
            }
            String planStr = content.split("```java")[1]
                    .split("```")[0]
                    .trim();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    planStr,
                    new TypeReference<>() {
                    }
            );
        } catch (Exception e) {
            log.error("❌ 解析计划时出错：{}", e.getMessage());
            log.info("原始响应：{}", content);
            return List.of();
        }
    }

}
