package com.coigniez.resumebuilder.templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.templates.layouts.LayoutTemplate;
import com.coigniez.resumebuilder.templates.layouts.StandardNavyBlueTemplate;

import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Component
public class LayoutTemplates {

    @Autowired
    private StandardNavyBlueTemplate standardNavyBlueTemplate;

    @Getter
    private final Map<String, LayoutTemplate> layoutTemplateMap = new HashMap<>();

    @PostConstruct
    private void init() {
        layoutTemplateMap.put(StandardNavyBlueTemplate.LAYOUT_NAME, LayoutTemplate.builder()
                .name(StandardNavyBlueTemplate.LAYOUT_NAME)
                .image(null)
                .layoutprompt(standardNavyBlueTemplate::generate)
                .build());
    }

    public LayoutTemplate getLayoutTemplateByName(String name) {
        return layoutTemplateMap.get(name);
    }

    public List<LayoutTemplate> getAllLayoutTemplates() {
        return layoutTemplateMap.values().stream().toList();
    }
}
