package org.example.dingwan.model.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Thinking {
    private String type = "enabled";
}
