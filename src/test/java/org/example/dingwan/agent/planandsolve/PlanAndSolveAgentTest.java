package org.example.dingwan.agent.planandsolve;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class PlanAndSolveAgentTest {

    @Autowired PlanAndSolveAgent planAndSolveAgent;

    @Test
    void run() {
        planAndSolveAgent.run("一个水果店周一卖出了15个苹果。周二卖出的苹果数量是周一的两倍。周三卖出的数量比周二少了5个。请问这三天总共卖出了多少个苹果？");
        /*
        *
            --- 开始处理问题 ---
            问题：一个水果店周一卖出了15个苹果。周二卖出的苹果数量是周一的两倍。周三卖出的数量比周二少了5个。请问这三天总共卖出了多少个苹果？

            --- 正在生成计划 ---
            ✔ 计划已生成：
            ```java
            ["确定周一卖出的苹果数量：15个", "计算周二卖出的苹果数量：周一的两倍，即15 * 2 = 30个", "计算周三卖出的苹果数量：比周二少5个，即30 - 5 = 25个", "计算三天总共卖出的苹果数量：15 + 30 + 25 = 70个"]
            ```

            --- 正在执行计划 ---
            -> 正在执行步骤 1/4: 确定周一卖出的苹果数量：15个
            ✔ 步骤 1 已完成，结果：15
            -> 正在执行步骤 2/4: 计算周二卖出的苹果数量：周一的两倍，即15 * 2 = 30个
            ✔ 步骤 2 已完成，结果：30
            -> 正在执行步骤 3/4: 计算周三卖出的苹果数量：比周二少5个，即30 - 5 = 25个
            ✔ 步骤 3 已完成，结果：25
            -> 正在执行步骤 4/4: 计算三天总共卖出的苹果数量：15 + 30 + 25 = 70个
            ✔ 步骤 4 已完成，结果：70

            --- 任务完成 ---
            最终答案：70
        * */
    }

}