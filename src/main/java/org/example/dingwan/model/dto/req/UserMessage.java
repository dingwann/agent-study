package org.example.dingwan.model.dto.req;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UserMessage extends Message {

    public UserMessage(String content) {
        super("user", content);
    }

}
