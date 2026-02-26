package org.example.dingwan.model.dto.res;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ChatResponseRoot {

    private String id;
    private List<Choices> choices;
    private Long created;
    private String model;
    private String object;
    private Usage usage;

}