package org.example.dingwan.tool;

import org.example.dingwan.agent.tool.AiParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class JsonSchemaUtils {

    /**
     * schema --> type:object,properties:properties; properties: type: , desc:
     * @param method
     * @return
     */
    public static Map<String, Object> generateSchema(Method method) {

        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");

        Map<String, Object> properties = new HashMap<>();

        // 获取方法的所有参数
        Parameter[] parameters = method.getParameters();

        for (Parameter param : parameters) {

            Map<String, Object> paramSchema = new HashMap<>();
            // 获取参数类型
            Class<?> type = param.getType();

            if (type == String.class) {
                paramSchema.put("type", "string");
            } else if (type == Integer.class || type == int.class) {
                paramSchema.put("type", "integer");
            } else if (type == Long.class || type == long.class) {
                paramSchema.put("type", "long");
            } else if (type == Boolean.class || type == boolean.class) {
                paramSchema.put("type", "boolean");
            } else {
                paramSchema.put("type", "object");
            }

            if (param.isAnnotationPresent(AiParam.class)) {
                AiParam p = param.getAnnotation(AiParam.class);
                paramSchema.put("description", p.description());
            }

            properties.put(param.getName(), paramSchema);
        }

        schema.put("properties", properties);

        return schema;
    }
}