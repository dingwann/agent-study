package org.example.dingwan.model.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMessage implements Message {
    private String role;
    private String content;

    public static UserMessage.UserMessageBuilder builder() {
        return new UserMessage.UserMessageBuilder().role("user");
    }
}
