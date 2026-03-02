package org.example.dingwan.agent.tool;

import org.example.dingwan.tool.JsonSchemaUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class ToolAnnotationScanner {

    private ToolRegistry registry;

    public ToolAnnotationScanner(ToolRegistry registry) {
        this.registry = registry;
    }

    public void scan(Object bean) {

        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(AiTool.class)) {

                AiTool toolAnn = method.getAnnotation(AiTool.class);

                String name = toolAnn.name().isEmpty()
                        ? method.getName()
                        : toolAnn.name();

                String desc = toolAnn.description();

                Map<String, Object> schema =
                        JsonSchemaUtils.generateSchema(method);

                ToolDefinition tool = new ToolDefinition(
                        name,
                        desc,
                        schema,
                        method,
                        bean
                );
                registry.register(tool);
            }
        }
    }
}