package com.coigniez.resumebuilder.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.ColumnService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("columns")
@RequiredArgsConstructor
@Tag(name = "Column")
public class ColumnController implements CrudController<ColumnRequest, ColumnResponse> {

    private final ColumnService columnService;

    @Override
    public ResponseEntity<Long> create(@Valid ColumnRequest request, Authentication user) {
        long id = columnService.create(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    public ResponseEntity<ColumnResponse> get(Long id, Authentication user) {
        ColumnResponse column = columnService.get(id);
        return ResponseEntity.ok(column);
    }

    @Override
    public ResponseEntity<Void> update(Long id, @Valid ColumnRequest request, Authentication user) {
        request.setId(id);
        columnService.update(request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> delete(Long id, Authentication user) {
        columnService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
}
