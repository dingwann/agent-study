package org.example.dingwan.model.dto.req;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Builder
@Data
public class ChatParamsRequest {
    private String model;
    private List<Message> messages;
    private int max_completion_tokens;
    private Thinking thinking;
}