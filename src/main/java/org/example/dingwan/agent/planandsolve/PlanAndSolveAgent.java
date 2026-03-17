package org.example.dingwan.agent.planandsolve;

import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.tool.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 为了凸显 Plan-and-Solve 范式在结构化推理任务上的优势，该智能体此处不使用工具。
 */
@Component
@Slf4j
public class PlanAndSolveAgent {

    private final ChatClient chatClient;
    private final Executor executor;
    private final Planner planner;

    public PlanAndSolveAgent(ChatClient chatClient, Executor executor, Planner planner) {
        this.chatClient = chatClient;
        this.executor = executor;
        this.planner = planner;
    }

    /**
     * 运行智能体的完整流程:先规划，后执行。
     * @param question 问题
     */
    public void run(String question) {
        log.info("\n--- 开始处理问题 ---\n问题：{}", question);
        // 调用规划器生成计划
        List<String> plan = this.planner.plan(question);
        if (plan.isEmpty()) {
            log.info("\n--- 任务中止 --- \n无法生成有效的行动计划");
            return;
        }
        // 调用执行器执行计划
        String execute = this.executor.execute(question, plan);
        log.info("\n --- 任务完成 --- \n最终答案：{}", execute);
    }

}
