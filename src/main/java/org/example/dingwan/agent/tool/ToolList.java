package org.example.dingwan.agent.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * SimpleAgent使用的工具  后续不再使用
 */
public class ToolList {

    private static final Map<String, String> toolMap = new HashMap<>();

    public static boolean isExist(String methodName) {
        return toolMap.containsKey(methodName);
    }

    public static void addTool(String methodName, String toolName) {
        toolMap.put(methodName, toolName);
    }

}
