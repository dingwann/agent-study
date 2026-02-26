package org.example.dingwan.controller;

import jakarta.annotation.Resource;
import org.example.dingwan.common.BaseResponse;
import org.example.dingwan.common.ResultUtils;
import org.example.dingwan.service.SimpleAgentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simple")
public class SimpleAgentController {

    @Resource
    private SimpleAgentService simpleAgentService;

    @GetMapping("/chat")
    public BaseResponse<String> chat(@RequestParam String query) {
        String chat = simpleAgentService.chat(query);
        return ResultUtils.success(chat);
    }

}
