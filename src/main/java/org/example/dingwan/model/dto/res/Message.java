package org.example.dingwan.model.dto.res;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Message {

    private String content;
    private String role;
    private Object toolCalls;
    private String reasoningContent;

}