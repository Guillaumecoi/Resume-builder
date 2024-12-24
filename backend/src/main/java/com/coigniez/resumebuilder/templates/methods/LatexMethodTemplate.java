package com.coigniez.resumebuilder.templates.methods;

import com.coigniez.resumebuilder.domain.latex.HasLatexMethod;
import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class LatexMethodTemplate {

    @NonNull
    private HasLatexMethod type;
    @NonNull
    private String methodName;
    @NonNull
    private String method;
    
    // Template attributes
    private String name;
    private String imagePath;

    /**
     * Convert this template to a CreateLatexMethodRequest object
     * 
     * @return CreateLatexMethodRequest
     */
    public CreateLatexMethodRequest toCreateLatexMethodRequest() {
        return CreateLatexMethodRequest.builder()
            .type(type)
            .name(methodName)
            .method(method)
            .build();
    }
}
