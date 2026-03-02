package org.example.dingwan.tool;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class WeatherTool {
    
    private final RestClient restClient;
    
    public WeatherTool(RestClient.Builder builder) {
        this.restClient = builder.build();
    }
    
    public String getWeather(String city) {
        try {
/*            // 发起网络请求
            WeatherResponse response = restClient.get()
                    .uri("https://wttr.in/{city}?format=j1", city)
                    .retrieve()
                    .body(WeatherResponse.class);
            
            // 提取当前天气状况
            CurrentCondition condition = response.getCurrentCondition().get(0);
            String weatherDesc = condition.getWeatherDesc().getFirst().getValue();
            String tempC = condition.getTempC();*/
            
            // 格式化成自然语言返回
            return String.format("%s当前天气:%s，气温%s摄氏度", city, "阴天", "16");
            
        } catch (RestClientException e) {
            // 处理网络错误
            return String.format("错误：查询天气时遇到网络问题 - %s", e.getMessage());
        } catch (Exception e) {
            // 处理数据解析错误
            return String.format("错误：解析天气数据失败，可能是城市名称无效 - %s", e.getMessage());
        }
    }
}