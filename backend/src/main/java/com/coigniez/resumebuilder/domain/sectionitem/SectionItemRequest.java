package com.coigniez.resumebuilder.domain.sectionitem;

import java.util.Map;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionItemRequest{

    @NotNull
    @NotEmpty
    String type;
    Integer itemOrder;
    @NotNull
    Map<String, Object> data;

}