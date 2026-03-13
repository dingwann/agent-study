package org.example.dingwan.agent.ReAct;


import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.agent.prompt.ReActPrompt;
import org.example.dingwan.agent.tool.ToolExecutor;
import org.example.dingwan.agent.tool.ToolRegistry;
import org.example.dingwan.model.dto.res.ChatResponseRoot;
import org.example.dingwan.tool.ChatClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ReActAgent {

    private final ChatClient  chatClient;
    private final ToolExecutor toolExecutor;
    private final ToolRegistry toolRegistry;
    private final int MAX_STEPS = 5;
    private List<String> history;

    public ReActAgent(ChatClient chatClient, ToolExecutor toolExecutor, ToolRegistry toolRegistry) {
        this.chatClient = chatClient;
        this.toolExecutor = toolExecutor;
        this.toolRegistry = toolRegistry;
    }


    public String run(String question) {
        // 清空历史记录
        history = new ArrayList<>();
        int currentStep = 0;

        while (currentStep <  MAX_STEPS) {
            currentStep++;
            log.info("--- 第{}步", currentStep);
            // 格式化提示词（将工具列表替换、历史记录追加用户问题）
            String toolList = this.toolRegistry.getAllToolDesc();
            String historyStr = String.join("\n", history);
            // 开始替换提示词模板
            String ReActPromptStr = ReActPrompt.REACT_PROMPT_TEMPLATE;
            String replace = ReActPromptStr.replace("${tools}", toolList);
            String replace1 = replace.replace("${question}", question);
            String PromptResult = replace1.replace("${history}", historyStr);
            // 调用LLM进行思考
            ChatResponseRoot chatResponse = this.chatClient.chat(question, PromptResult);
            if (chatResponse == null) {
                log.debug("大模型调用失败");
                break;
            }
            // 解析、执行、整合
            String chatResponseStr = chatResponse.getChoices().get(0).getMessage().getContent();
            String chatOutputSchema = parseOutput(chatResponseStr);
            Map<String, Object> chatActionStr = parseAction(chatOutputSchema);

        }
        return question;
    }

    /**
     * 解析大模型的格式化输出内容：
     * Thought: 你的思考过程，用于分析问题、拆解任务和规划下一步行动。
     * Action: 你决定采取的行动，必须是以下格式之一:
     * - `tool_name[{ "param1": "value1", "param2": "value2" }]` : 调用一个可用工具
     * - `Finish[最终答案]` : 当你认为已经获得最终答案时
     * @param output 大模型输入
     * @return tool_name[{ "param1": "value1", "param2": "value2" }] / Finish[最终答案]
     */
    private String parseOutput(String output) {
        return output;
    }

    /**
     * 解析行动
     * @param output tool_name[{ "param1": "value1", "param2": "value2" }] / Finish[最终答案]
     * @return Map<String, Object>：toolName,Params
     */
    private Map<String, Object> parseAction(String output) {
        return Map.of();
    }

}
