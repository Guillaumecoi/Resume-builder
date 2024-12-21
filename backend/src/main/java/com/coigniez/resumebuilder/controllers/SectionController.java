package com.coigniez.resumebuilder.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResponse;
import com.coigniez.resumebuilder.domain.section.dtos.UpdateSectionRequest;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.SectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("sections")
@RequiredArgsConstructor
@Tag(name = "Resume Sections")
public class SectionController
        implements CrudController<CreateSectionRequest, UpdateSectionRequest, SectionResponse, Long> {

    private final SectionService sectionService;

    @Override
    @Operation(operationId = "createSection")
    public ResponseEntity<Long> create(@Valid @RequestBody CreateSectionRequest request, Authentication user) {
        Long id = sectionService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    @Operation(operationId = "getSection")
    public ResponseEntity<SectionResponse> get(@PathVariable Long id, Authentication user) {
        SectionResponse section = sectionService.get(id);
        return ResponseEntity.ok(section);
    }

    @Override
    @Operation(operationId = "updateSection")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdateSectionRequest request,
            Authentication user) {
        request.setId(id);
        sectionService.update(request);
        return ResponseEntity.ok().build();
    }

    @Override
    @Operation(operationId = "deleteSection")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication user) {
        sectionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
