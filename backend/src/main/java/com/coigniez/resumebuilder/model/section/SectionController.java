package com.coigniez.resumebuilder.model.section;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
@RequestMapping("resumes/{resumeId}/sections")
@RequiredArgsConstructor
@Tag(name = "Resume Sections")
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<Long> create(@PathVariable Long resumeId, @Valid @RequestBody SectionRequest request, Authentication user) {
        Long id = sectionService.create(resumeId, request, user);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionResponse> getSection(@PathVariable Long resumeId, @PathVariable Long id, Authentication user) {
        SectionResponse section = sectionService.get(id, user);
        return ResponseEntity.ok(section);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long resumeId, @PathVariable Long id, @Valid @RequestBody SectionRequest request, Authentication user) {
        sectionService.update(id, request, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long resumeId, @PathVariable Long id, Authentication user) {
        sectionService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
    
}
