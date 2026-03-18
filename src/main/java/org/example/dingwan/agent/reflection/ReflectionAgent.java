package org.example.dingwan.agent.reflection;


import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.agent.prompt.ReflectionPrompt;
import org.example.dingwan.agent.reflection.memory.SimpleMemory;
import org.example.dingwan.enums.RecordType;
import org.example.dingwan.tool.ChatClient;
import org.springframework.stereotype.Component;

/**
 * 来源于人类学习过程 进行执行-反思-优化的过程
 */
@Component
@Slf4j
public class ReflectionAgent {

    private final ChatClient chatClient;
    private final SimpleMemory simpleMemory;
    private final int maxSteps = 5;

    public ReflectionAgent(ChatClient chatClient, SimpleMemory simpleMemory) {
        this.chatClient = chatClient;
        this.simpleMemory = simpleMemory;
    }

    public String run(String task) {
        log.info("\n--- 开始处理任务 ---\n任务：{}", task);

        // 初始执行
        log.info("\n--- 正在进行初始尝试 ---");
        String initialPrompt = ReflectionPrompt.INITIAL_PROMPT_TEMPLATE.replace("{task}", task);
        String initialCode = this.chatClient.chat(initialPrompt).getChoices().get(0).getMessage().getContent();
        log.info("\n--- 初始代码 ---\n{}", initialCode);
        this.simpleMemory.addRecord(RecordType.execution.name(), initialCode);

        // 迭代反思
        for (int i = 0; i < this.maxSteps; i++) {
            log.info("\n--- 第 {}/{} 轮迭代", i + 1, this.maxSteps);
            // 反思
            log.info("\n-> 正在进行反思");
            String lastExecution = this.simpleMemory.getLastExecution();
            String reflectPrompt = ReflectionPrompt.REFLECT_PROMPT_TEMPLATE
                    .replace("{task}", task)
                    .replace("{code}", lastExecution);
            String feedback = this.chatClient.chat(reflectPrompt).getChoices().get(0).getMessage().getContent();
            this.simpleMemory.addRecord(RecordType.reflection.name(), feedback);

            //检查是否需要停止
            if (feedback.contains("无需改进")) {
                log.info("\n✅ 反思认为代码已无需改进，任务完成。");
                break;
            }

            // 优化
            log.info("\n-> 正在进行优化...");
            String refinePrompt = ReflectionPrompt.REFINE_PROMPT_TEMPLATE
                    .replace("{task}", task)
                    .replace("{last_code_attempt}", lastExecution)
                    .replace("{feedback}", feedback);
            String refinedCode = this.chatClient.chat(refinePrompt).getChoices().get(0).getMessage().getContent();
            this.simpleMemory.addRecord(RecordType.execution.name(), refinedCode);
        }

        // 完成
        String lastExecution = this.simpleMemory.getLastExecution();
        log.info("\n--- 任务完成 ---\n最终生成的代码:\n```python\n{}\n```", lastExecution);
        return lastExecution;
    }

}
