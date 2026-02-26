package org.example.dingwan.model.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemMessage implements Message {
    private String role;
    private String content;

    public static SystemMessageBuilder builder() {
        return new SystemMessageBuilder().role("system");
    }
}
