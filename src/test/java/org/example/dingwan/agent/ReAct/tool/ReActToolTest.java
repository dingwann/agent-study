package org.example.dingwan.agent.ReAct.tool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReActToolTest {

    @Autowired
    ReActTool reActTool;

    @Test
    void search() {
        String search = reActTool.search("英伟达最新的GPU型号是什么");
        System.out.println(search);
    }

}