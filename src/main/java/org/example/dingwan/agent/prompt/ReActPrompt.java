package org.example.dingwan.agent.prompt;

/**
 * 适合Java版本的提示词 将工具的参数列表使用json格式限制 参数描述更具体适合反射
 */
public class ReActPrompt {

    public static final String REACT_PROMPT_TEMPLATE = """
            请注意，你是一个有能力调用外部工具的智能助手，当你搜集到足够的信息时就可以总结答案进行回答了。
            可用工具如下:
            {tools}
            请严格按照以下格式进行回应:
            Thought: 你的思考过程，用于分析问题、拆解任务和规划下一步行动。
            Action: 你决定采取的行动，必须是以下格式之一:
            - `tool_name[{ "param1": "value1", "param2": "value2" }]` : 调用一个可用工具
            - `Finish[最终答案]` : 当你认为已经获得最终答案时
            json 参数规则：
            1. 必须使用 JSON 对象
            2. key 必须是参数名
            3. value 是参数值
            格式回应示例：
            - 示例1
            Thought: 我需要查询2026年的行业趋势，需要调用search工具。
            Action: search[{ "query": "2026年的行业趋势" }]
            - 示例2
            Thought: 我已经搜集到足够的信息了，可以回答用户问题了。
            Action: Finish[最终答案]: 答案。
            现在，请开始解决以下问题:
            Question: {question}
            History: {history}
            """;

}
