package org.example.dingwan.agent.ReAct.tool;


import lombok.extern.slf4j.Slf4j;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.example.dingwan.annotation.AiParam;
import org.example.dingwan.annotation.AiTool;
import org.springframework.stereotype.Component;


/**
 * 计算工具类 支持复杂运算
 */
@Component
@Slf4j
public class CalcTool {

    @AiTool(name = "calculator", description = """
            用于计算数学表达式并返回精确结果。
            
            支持：
            - 基本运算：加减乘除（+ - * /）
            - 括号优先级
            - 多步骤复杂表达式
            
            当问题涉及精确计算时必须使用该工具，而不是依赖模型自行推理。
            
            输入应为一个合法的数学表达式字符串。
            """)
    public double calculate(@AiParam("合法的数学计算表达式字符串") String expression) {
        // 1. 预处理
        expression = normalize(expression);

        // 2. 构建表达式
        Expression expression_result = new ExpressionBuilder(expression).build();

        // 3. 计算
        return expression_result.evaluate();
    }

    private static String normalize(String expr) {
        return expr
                .replace("×", "*")
                .replace("÷", "/")
                .replace(" ", "");
    }

}
