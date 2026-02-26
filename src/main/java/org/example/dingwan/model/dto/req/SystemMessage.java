package org.example.dingwan.model.dto.req;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class SystemMessage extends Message {

    public SystemMessage(String content) {
        super("system", content);
    }

}
