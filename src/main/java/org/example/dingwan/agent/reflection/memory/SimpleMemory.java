package org.example.dingwan.agent.reflection.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 一个简单的短期记忆模块，用于存储智能体的行动与反思轨迹。
 */
@Component
@Slf4j
public class SimpleMemory {

    private final List<Map<String, Object>> records = new ArrayList<>();

    /**
     * 向记忆中添加记录
     * @param recordType 记录的类型 ('execution' 或 'reflection')。
     * @param content 记录的具体内容 (例如，生成的代码或反思的反馈)。
     */
    public void addRecord(String recordType, String content) {
        Map<String, Object> record = new HashMap<>();
        record.put("type", recordType);
        record.put("content", content);
        this.records.add(record);
        log.info("📝 记忆已更新，新增一条{}记录", recordType);
    }

    /**
     * 将所有记忆格式化成一个连贯的字符串文本，用于构建提示词。
     * @return 文本
     */
    public String getTrajectory() {
        List<String> trajectoryParts = new ArrayList<>();
        this.records.forEach(record -> {
            if (record.get("type").equals("execution")) {
                trajectoryParts.add(String.format("--- 上一轮尝试（代码） ---\n%s", record.get("content")));
            } else if (record.get("type").equals("reflection")) {
                trajectoryParts.add(String.format("--- 评审员反馈 ---\n%s", record.get("content")));
            }
        });
        return String.join("\n\n", trajectoryParts);
    }

    /**
     * 获取最近一次的执行结果 (例如，最新生成的代码)。
     * 如果不存在，则返回 null。
     */
    public String getLastExecution() {
        for (int i = records.size() - 1; i >= 0; i--) {
            Map<String, Object> record = records.get(i);
            if ("execution".equals(record.get("type"))) {
                return (String) record.get("content");
            }
        }
        return null;
    }

}
