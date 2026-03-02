package org.example.dingwan.agent.tool;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ToolRegistry {

    private final Map<String, ToolDefinition> toolMap = new ConcurrentHashMap<>();

    public void register(ToolDefinition tool) {
        toolMap.put(tool.getName(), tool);
    }

    public ToolDefinition get(String name) {
        return toolMap.get(name);
    }

    public Collection<ToolDefinition> getAll() {
        return toolMap.values();
    }
}