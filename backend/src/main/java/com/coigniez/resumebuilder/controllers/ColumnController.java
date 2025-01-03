package com.coigniez.resumebuilder.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResponse;
import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.column.dtos.UpdateColumnRequest;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.ColumnService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("columns")
@RequiredArgsConstructor
@Tag(name = "Column")
public class ColumnController implements CrudController<CreateColumnRequest, UpdateColumnRequest, ColumnResponse, Long> {

    private final ColumnService columnService;

    @Override
    @Operation(operationId = "createColumn")
    public ResponseEntity<Long> create(@Valid CreateColumnRequest request, Authentication user) {
        long id = columnService.create(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    @Operation(operationId = "getColumn")
    public ResponseEntity<ColumnResponse> get(Long id, Authentication user) {
        ColumnResponse column = columnService.get(id);
        return ResponseEntity.ok(column);
    }

    @Override
    @Operation(operationId = "updateColumn")
    public ResponseEntity<Void> update(Long id, UpdateColumnRequest request, Authentication user) {
        request.setId(id);
        columnService.update(request);
        return ResponseEntity.ok().build();
    }

    @Override
    @Operation(operationId = "deleteColumn")
    public ResponseEntity<Void> delete(Long id, Authentication user) {
        columnService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
}
