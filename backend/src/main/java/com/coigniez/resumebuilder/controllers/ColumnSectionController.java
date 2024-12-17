package com.coigniez.resumebuilder.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.ColumnSectionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("columnsections")
@RequiredArgsConstructor
@Tag(name = "ColumnSection")
public class ColumnSectionController implements CrudController<ColumnSectionRequest, ColumnSectionResponse> {

    private final ColumnSectionService columnSectionService;
    
    @Override
    public ResponseEntity<Long> create(@Valid ColumnSectionRequest request, Authentication user) {
        Long id = columnSectionService.create(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    public ResponseEntity<ColumnSectionResponse> get(Long id, Authentication user) {
        ColumnSectionResponse columnSection = columnSectionService.get(id);
        return ResponseEntity.ok(columnSection);
    }

    @Override
    public ResponseEntity<Void> update(Long id, @Valid ColumnSectionRequest request, Authentication user) {
        request.setId(id);
        columnSectionService.update(request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> delete(Long id, Authentication user) {
        columnSectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
