package org.example.dingwan.service.impl;


import jakarta.annotation.Resource;
import org.example.dingwan.agent.SimpleAgent;
import org.example.dingwan.service.SimpleAgentService;
import org.springframework.stereotype.Service;

@Service
public class SimpleAgentServiceImpl implements SimpleAgentService {

    @Resource
    private SimpleAgent simpleAgent;

    @Override
    public String chat(String query) {
        try {
            return simpleAgent.execute(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
