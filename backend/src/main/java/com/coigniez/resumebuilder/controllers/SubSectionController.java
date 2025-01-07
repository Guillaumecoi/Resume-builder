package com.coigniez.resumebuilder.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionCreateReq;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionResp;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionUpdateReq;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.SubSectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@Slf4j
@RestController
@RequestMapping("subsections")
@RequiredArgsConstructor
@Tag(name = "Resume Subsections")
public class SubSectionController
        implements CrudController<SubSectionCreateReq, SubSectionUpdateReq, SubSectionResp, Long> {

    private final SubSectionService subSectionService;

    @Override
    @Operation(operationId = "createSubSection")
    public ResponseEntity<Long> create(@Valid SubSectionCreateReq request, Authentication user) {
        Long id = subSectionService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    @Operation(operationId = "getSubSection")
    public ResponseEntity<SubSectionResp> get(Long id, Authentication user) {
        SubSectionResp subSection = subSectionService.get(id);
        return ResponseEntity.ok(subSection);
    }

    @Override
    @Operation(operationId = "updateSubSection")
    public ResponseEntity<Void> update(Long id, SubSectionUpdateReq request, Authentication user) {
        request.setId(id);
        subSectionService.update(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Operation(operationId = "deleteSubSection")
    public ResponseEntity<Void> delete(Long id, Authentication user) {
        subSectionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
