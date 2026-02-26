package org.example.dingwan.agent.tool;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.dingwan.exception.BusinessException;
import org.example.dingwan.exception.ErrorCode;
import org.example.dingwan.exception.ThrowUtils;
import org.example.dingwan.model.entity.JsonRootBean;
import org.example.dingwan.model.entity.Results;
import org.example.dingwan.tool.TavilyTool;
import org.example.dingwan.tool.WeatherTool;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SimpleTool {

    private final WeatherTool weatherTool;
    private final TavilyTool tavilyTool;

    public SimpleTool(WeatherTool weatherTool, TavilyTool tavilyTool) {
        this.weatherTool = weatherTool;
        this.tavilyTool = tavilyTool;
    }

    @PostConstruct
    public void init() {
        log.info("SimpleTool初始化，注册工具列表中...");
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            ToolList.addTool(method.getName(), method.getName());
        }
        log.info("SimpleTool注册工具列表完成");
    }

    /**
     * 通过调用 wttr.in API 查询真实的天气信息。
     * @param city --> 城市名
     * @return 字符串结果
     */
    public String getWeather(String city) {
        return weatherTool.getWeather(city);
    }

    /**
     * 根据城市和天气，使用Tavily Search API搜索并返回优化后的景点推荐。
     * @param city 城市名
     * @param weather 天气
     * @return 字符串结果
     */
    public String getAttraction(String city, String weather) {
        String query = String.format("%s在%s天气下最值得去的旅游景点推荐及理由", city, weather);
        try {
            JsonRootBean search = tavilyTool.search(query);
            ThrowUtils.throwIf(search == null, ErrorCode.OPERATION_ERROR, "Tavily工具调用出错");

            if (search.getAnswer() != null)
                return search.getAnswer();
            // 如果没有综合性答案 格式化原结果
            List<String> formattedResults = new ArrayList<>();
            for (Results results : search.getResults()) {
                formattedResults.add(String.format("- %s:%s", results.getTitle(), results.getContent()));
            }
            if (formattedResults.isEmpty())
                return "抱歉，没有找到相关的旅游景点推荐。";
            return String.format("根据搜索，为您找到以下信息:\n%s", String.join("\n", formattedResults));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

}
