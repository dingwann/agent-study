package org.example.dingwan.model.dto.res;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PromptTokensDetails {

    private Long cachedTokens;

}