package org.example.dingwan.model.dto.res;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Usage {

    private Long completionTokens;
    private Long promptTokens;
    private Long totalTokens;
    private CompletionTokensDetails completionTokensDetails;
    private PromptTokensDetails promptTokensDetails;

}