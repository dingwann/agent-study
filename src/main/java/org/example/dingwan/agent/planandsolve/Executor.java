package org.example.dingwan.agent.planandsolve;

import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.agent.prompt.PlanAndSolvePrompt;
import org.example.dingwan.tool.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class Executor {

    private final ChatClient chatClient;

    public Executor(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * 根据计划，逐步执行并解决问题
     * @param question 用户问题
     * @param plan 大模型规划的计划
     * @return 返回当前步骤的执行结果
     */
    public String execute(String question, List<String> plan) {
        // 存储历史步骤和结果的字符串
        String history = "";
        String finalAnswer = "";

        log.info("\n --- 正在执行计划 ---");
        for (int i = 0; i < plan.size(); i++) {
            log.info("\n-> 正在执行步骤 {}/{}: {}", i + 1, plan.size(), plan.get(i));
            // 替换提示词模板
            String prompt = PlanAndSolvePrompt.EXECUTOR_PROMPT_TEMPLATE
                    .replace("{question}", question)
                    .replace("{plan}", plan.toString())
                    .replace("{history}", history.isEmpty() ? "无" : history)
                    .replace("{current_step}", plan.get(i));
            // 执行
            String responseText = this.chatClient.chat(prompt).getChoices().get(0).getMessage().getContent();
            if (i + 1 ==  plan.size())
                finalAnswer = responseText;
            // 更新历史记录
            history += String.format("步骤：%s：%s\n结果：%s\n\n", i + 1, plan.get(i), responseText);
            log.info("✔ 步骤 {} 已完成，结果：{}", i + 1, responseText);
        }

        // 循环结束后 最后一步的响应就是答案
        return finalAnswer;
    }

}
