package org.example.dingwan.model.dto.res;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Choices {

    private String finishReason;
    private Long index;
    private Message message;

}