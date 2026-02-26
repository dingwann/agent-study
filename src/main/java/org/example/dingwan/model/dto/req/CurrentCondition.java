package org.example.dingwan.model.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.dingwan.model.dto.WeatherDesc;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentCondition {
    @JsonProperty("weatherDesc")
    private List<WeatherDesc> weatherDesc;
    
    @JsonProperty("temp_C")
    private String tempC;

}