package com.coigniez.resumebuilder.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.domain.section.SectionResponse;
import com.coigniez.resumebuilder.services.SectionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("sections")
@RequiredArgsConstructor
@Tag(name = "Resume Sections")
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody SectionRequest request, Authentication user) {
        Long id = sectionService.create(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionResponse> getSection(@PathVariable Long id, Authentication user) {
        SectionResponse section = sectionService.get(id);
        return ResponseEntity.ok(section);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody SectionRequest request, Authentication user) {
        request.setId(id);
        sectionService.update(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication user) {
        sectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
