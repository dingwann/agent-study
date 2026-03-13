package org.example.dingwan.agent.ReAct.tool;

import org.example.dingwan.agent.tool.ToolExecutor;
import org.example.dingwan.agent.tool.ToolRegistry;
import org.example.dingwan.model.dto.res.ChatResponseRoot;
import org.example.dingwan.tool.ChatClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class ReActToolTest {

    @Autowired ReActTool reActTool;

    @Autowired ToolExecutor toolExecutor;

    @Autowired ToolRegistry toolRegistry;

    @Autowired ChatClient chatClient;

    @Test
    void search() {
        String search = reActTool.search("英伟达最新的GPU型号是什么");
        System.out.println(search);
    }

    @Test
    void printTool() {
        String allToolDesc = toolRegistry.getAllToolDesc();
        System.out.println(allToolDesc);
    }

    @Test
    void executeTool() throws Exception {
        String tool_name = "search";
        Map<String, Object> params = Map.of("query", "英伟达最新的GPU型号是什么");
        Object execute = toolExecutor.execute(tool_name, params);
        System.out.println(execute);
    }

    @Test
    void chat() {
        ChatResponseRoot chat = chatClient.chat("你好");
        System.out.println(chat);
    }

}