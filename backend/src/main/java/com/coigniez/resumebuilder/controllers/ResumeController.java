package com.coigniez.resumebuilder.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.common.PageResponse;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeSimpleResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeUpdateReq;
import com.coigniez.resumebuilder.domain.resume.enums.ResumeOrderBy;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.LayoutService;
import com.coigniez.resumebuilder.services.ResumeService;
import com.coigniez.resumebuilder.services.SectionService;
import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;
import java.util.Arrays;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("resumes")
@RequiredArgsConstructor
@Tag(name = "Resume")
public class ResumeController
        implements CrudController<ResumeCreateReq, ResumeUpdateReq, ResumeResp, Long> {

    private final ResumeService resumeService;
    private final SectionService sectionService;
    private final LayoutService layoutService;

    @Override
    @PostMapping
    @Operation(operationId = "createResume")
    public ResponseEntity<Long> create(@Valid @RequestBody ResumeCreateReq request, Authentication user) {
        Long id = resumeService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(operationId = "getResume")
    public ResponseEntity<ResumeResp> get(@PathVariable Long id, Authentication user) {
        ResumeResp resume = resumeService.get(id);
        return ResponseEntity.ok(resume);
    }

    @Override
    @PostMapping("/{id}")
    @Operation(operationId = "updateResume")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ResumeUpdateReq request,
            Authentication user) {
        request.setId(id);
        resumeService.update(request);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/{id}/delete")
    @Operation(operationId = "deleteResume")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication user) {
        resumeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(operationId = "getAllResumes")
    public ResponseEntity<PageResponse<ResumeSimpleResp>> getAllResumes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "order", defaultValue = "LAST_MODIFIED_DATE") ResumeOrderBy order,
            @RequestParam(name = "direction", defaultValue = "DESC") Sort.Direction direction,
            Authentication connectedUser) {
        return ResponseEntity.ok(resumeService.getAll(page, size, order, direction));
    }

    @PostMapping("/deleteAll")
    @Operation(operationId = "deleteAllResumes")
    public ResponseEntity<Void> deleteAllResumes(Authentication user) {
        resumeService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/{type}")
    @Operation(operationId = "getResumeChildren")
    public ResponseEntity<List<?>> getChildren(
        @PathVariable Long id, 
        @PathVariable(value = "type") @Pattern(regexp = ChildType.REGEX) String typeStr,
        Authentication user) {
        ChildType type = ChildType.fromString(typeStr);
        return switch(type) {
            case SECTION -> ResponseEntity.ok(sectionService.getAllByParentId(id));
            case LAYOUT -> ResponseEntity.ok(layoutService.getAllByParentId(id));
        };
    }

    @PostMapping("/{id}/delete/{type}")
    @Operation(operationId = "deleteResumeChildren")
    public ResponseEntity<Void> deleteChildren(
        @PathVariable Long id, 
        @PathVariable(value = "type") @Pattern(regexp = ChildType.REGEX) String typeStr,
        Authentication user) {
        switch(ChildType.fromString(typeStr)) {
            case SECTION -> sectionService.removeAllByParentId(id);
            case LAYOUT -> layoutService.removeAllByParentId(id);
        }
        return ResponseEntity.noContent().build();
    }
    
    @Getter
    @RequiredArgsConstructor
    public enum ChildType {
        SECTION("sections"),
        LAYOUT("layouts");
    
        private final String value;
        private static final String REGEX = "^(sections|layouts)$";
    
        @JsonCreator
        public static ChildType fromString(String value) {
            return Arrays.stream(values())
                .filter(type -> type.value.equals(value.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown child type: " + value));
        }
    }
    
}