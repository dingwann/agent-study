package org.example.dingwan.agent.ReAct;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.agent.prompt.ReActPrompt;
import org.example.dingwan.agent.tool.ToolExecutor;
import org.example.dingwan.agent.tool.ToolRegistry;
import org.example.dingwan.model.dto.res.ChatResponseRoot;
import org.example.dingwan.tool.ChatClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ReActAgent {

    private static final Pattern THOUGHT_PATTERN =
            Pattern.compile("Thought:\\s*(.*?)(?=\\nAction:|$)", Pattern.DOTALL);

    private static final Pattern ACTION_PATTERN =
            Pattern.compile("Action:\\s*(.*?)$", Pattern.DOTALL);

    private static final Pattern ACTION_CALL_PATTERN =
            Pattern.compile("(\\w+)\\[(.*)]", Pattern.DOTALL);

    private final ChatClient  chatClient;
    private final ToolExecutor toolExecutor;
    private final ToolRegistry toolRegistry;
    private final int MAX_STEPS = 6;
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
            String PromptResult = ReActPromptStr
                    .replace("{tools}", toolList)
                    .replace("{question}", question)
                    .replace("{history}", historyStr);
            // 调用LLM进行思考
            ChatResponseRoot chatResponse = this.chatClient.chat(question, PromptResult, 0.6);
            if (chatResponse == null) {
                log.debug("大模型调用失败");
                break;
            }
            // 解析、执行、整合
            String chatResponseStr = chatResponse.getChoices().get(0).getMessage().getContent();
            Map<String, Object> chatOutputSchema = parseOutput(chatResponseStr);
            Map<String, Object> chatActionStr = parseAction(chatOutputSchema.get("action").toString());
            if (chatActionStr.containsKey("finish")) {
                // 任务已经完成
                log.info("🎉 最终答案：{}", chatActionStr.get("finish"));
                return chatActionStr.get("finish").toString();
            }
            if (!chatActionStr.containsKey("toolName") || !chatActionStr.containsKey("Params")) {
                log.info("大模型输出格式或结果不正确，准备重试。");
                continue;
            }
            // 执行工具
            String toolName = chatActionStr.get("toolName").toString();
            String params = chatActionStr.get("Params").toString();
            String observation;
            log.info("💿 行动：{}[{}]", toolName, params);

            if (toolRegistry.get(toolName) == null) {
                log.info("错误：未找到名为{}的工具", toolName);
                observation = String.format("错误：未找到名为%s的工具", toolName);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> paramsMap = gson.fromJson(params, type);
            try {
                observation = toolExecutor.execute(toolName, paramsMap).toString();
            } catch (Exception e) {
                log.debug("❌ 工具执行出错，正在重试...");
                observation = String.format("工具：%s执行出错了，需要你重试", toolName);
            }
            log.info("👀 观察：{}", observation);
            this.history.add(String.format("Action: %s", chatActionStr));
            this.history.add(String.format("Observation: %s", observation));
        }
        log.info("循环已到最大步数，终止流程。");
        return null;
    }

    /**
     * 解析大模型的格式化输出内容：
     * Thought: 你的思考过程，用于分析问题、拆解任务和规划下一步行动。
     * Action: 你决定采取的行动，必须是以下格式之一:
     * - `tool_name[{ "param1": "value1", "param2": "value2" }]` : 调用一个可用工具
     * - `Finish[最终答案]` : 当你认为已经获得最终答案时
     * @param output 大模型输入
     * @return Map
     */
    private Map<String, Object> parseOutput(String output) {
        Map<String, Object> map = new HashMap<>();
        Matcher thoughtMatcher = THOUGHT_PATTERN.matcher(output);
        if (thoughtMatcher.find()) {
            map.put("thought", thoughtMatcher.group(1).trim());
        }
        Matcher actionMatcher = ACTION_PATTERN.matcher(output);
        if (actionMatcher.find()) {
            map.put("action", actionMatcher.group(1).trim());
        }
        return map;
    }

    /**
     * 解析行动
     * @param actionStr tool_name[{ "param1": "value1", "param2": "value2" }] / Finish[最终答案]
     * @return Map<String, Object>：toolName,Params
     */
    private Map<String, Object> parseAction(String actionStr) {
        if (actionStr.toLowerCase().contains("finish")) {
            return Map.of("finish", actionStr.substring(actionStr.indexOf(":")));
        }

        Matcher matcher = ACTION_CALL_PATTERN.matcher(actionStr);

        if (matcher.find()) {
            String toolName = matcher.group(1);
            String toolInput = matcher.group(2);
            return Map.of("toolName", toolName, "Params", toolInput);
        }
        return Map.of();
    }

}
