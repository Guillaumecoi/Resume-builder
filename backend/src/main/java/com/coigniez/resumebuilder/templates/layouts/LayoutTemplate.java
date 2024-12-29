package com.coigniez.resumebuilder.templates.layouts;

import java.util.function.Function;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutTemplate {

    private String name;
    private byte[] image;
    private Function<String, Long> layoutprompt;
    
}
