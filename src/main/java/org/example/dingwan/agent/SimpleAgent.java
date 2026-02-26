package org.example.dingwan.agent;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.agent.prompt.SimplePrompt;
import org.example.dingwan.agent.tool.SimpleTool;
import org.example.dingwan.agent.tool.ToolList;
import org.example.dingwan.exception.BusinessException;
import org.example.dingwan.exception.ErrorCode;
import org.example.dingwan.exception.ThrowUtils;
import org.example.dingwan.service.ChatService;
import org.example.dingwan.tool.TavilyTool;
import org.example.dingwan.tool.WeatherTool;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class SimpleAgent {

    @Resource
    private ChatService chatService;
    @Resource
    private WeatherTool weatherTool;
    @Resource
    private TavilyTool tavilyTool;

    private final List<String> promptList = new ArrayList<>();

    private static final Pattern THOUGHT_ACTION_PATTERN = Pattern.compile(
            "(Thought:.*?Action:.*?)(?=\\n\\s*(?:Thought:|Action:|Observation:)|\\Z)",
            Pattern.DOTALL
    );
    private static final Pattern ACTION_PATTERN = Pattern.compile(
            "Action: (.*)",
            Pattern.DOTALL
    );
    private static final Pattern FINISH_PATTERN = Pattern.compile("Finish\\[(.*)\\]");
    private static final Pattern TOOL_NAME_PATTERN = Pattern.compile("(\\w+)\\(");
    private static final Pattern ARGS_CONTENT_PATTERN = Pattern.compile("\\((.*)\\)");
    private static final Pattern KV_PATTERN = Pattern.compile("(\\w+)=\"([^\"]*)\"");

    public void execute(String prompt) throws Exception {
        log.info("SimpleAgent start");
        // 初始化
        promptList.add(String.format("用户请求：%s", prompt));
        // 开始循环（最大循环5次）
        for (int i = 0; i < 5; i++) {
            log.info("---- 循环" + (i + 1) + "----\n");
            // 构建prompt
            String fullPrompt = String.join("\n", promptList);
            // 调用llm进行思考
            String llmGenerate = chatService.generate(fullPrompt, SimplePrompt.AGENT_SYSTEM_PROMPT);
            // 模型可能产生多余的Thought-Action 需要截断
            Matcher matcher = THOUGHT_ACTION_PATTERN.matcher(llmGenerate);
            // 执行搜索
            if (matcher.find()) {
                // 获取分组并去除首尾空白 (对应 match.group(1).strip())
                String truncated = matcher.group(1).trim();
                // 比较并截断
                if (!truncated.equals(llmGenerate.trim())) {
                    llmGenerate = truncated;
                    log.info("已截断多余的 Thought-Action 对");
                }
            }
            // 打印日志
            log.info("模型输出:\n" + llmGenerate + "\n");
            // 添加到历史
            promptList.add(llmGenerate);
            // 解析并行动
            String observation;
            Matcher matchered = ACTION_PATTERN.matcher(llmGenerate);
            boolean b = matchered.find();
            if (!b) {
                observation = "错误: 未能解析到 Action 字段。请确保你的回复严格遵循 'Thought: ... Action: ...' 的格式。";
                String format = String.format("Observation: %s", observation);
                promptList.add(format);
                continue;
            }
            String actionStr = matchered.group(1).trim();
            if (actionStr.startsWith("Finish")) {
                String finalAnswer = FINISH_PATTERN.matcher(llmGenerate).group(1).trim();
                log.info("任务完成，最终答案：{}", finalAnswer);
                break;
            }
            // 执行llm的行动
            String toolName = TOOL_NAME_PATTERN.matcher(actionStr).group(1);
            String argStr = ARGS_CONTENT_PATTERN.matcher(actionStr).group(1);
            Map<String, String> kwargs = new HashMap<>();
            if (argStr != null) {
                Matcher kvMatcher = KV_PATTERN.matcher(argStr);
                while (kvMatcher.find()) {
                    kwargs.put(kvMatcher.group(1), kvMatcher.group(2));
                }
            }
            if (!ToolList.isExist(toolName))
                observation = String.format("错误，未定义的工具{%s}", toolName);
            observation = execTool(toolName, kwargs);
            String observationStr = String.format("Observation：%s", observation);
            log.info("observationStr\n");
            promptList.add(observationStr);
        }
    }

    private String execTool(String toolName, Map<String, String> kwargs) throws Exception {
        Class<SimpleTool> clazz = SimpleTool.class;
        Constructor<?> constructor = clazz.getConstructor(WeatherTool.class, TavilyTool.class);
        Object obj = constructor.newInstance(weatherTool, tavilyTool);

        int paramCount = kwargs.size();
        Method execMethod = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(toolName)
                    && method.getParameterCount() == paramCount) {
                execMethod = method;
            }
        }
        return execMethod.invoke(obj, kwargs.values()).toString();
    }

}
