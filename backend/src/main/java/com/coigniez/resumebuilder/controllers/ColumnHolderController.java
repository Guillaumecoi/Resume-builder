package com.coigniez.resumebuilder.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderResp;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderUpdateReq;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.ColumnHolderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("columnholders")
@RequiredArgsConstructor
@Tag(name = "ColumnHolder")
public class ColumnHolderController 
        implements CrudController<ColumnHolderCreateReq, ColumnHolderUpdateReq, ColumnHolderResp, Long> {

    private final ColumnHolderService columnHolderService;

    @Override
    @Operation(operationId = "createColumnHolder")
    public ResponseEntity<Long> create(@Valid ColumnHolderCreateReq request, Authentication user) {
        Long id = columnHolderService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    @Operation(operationId = "getColumnHolder") 
    public ResponseEntity<ColumnHolderResp> get(Long id, Authentication user) {
        ColumnHolderResp columnHolder = columnHolderService.get(id);
        return ResponseEntity.ok(columnHolder);
    }

    @Override
    @Operation(operationId = "updateColumnHolder")
    public ResponseEntity<Void> update(Long id, ColumnHolderUpdateReq request, Authentication user) {
        request.setId(id);
        columnHolderService.update(request);
        return ResponseEntity.ok().build();
    }

    @Override
    @Operation(operationId = "deleteColumnHolder")
    public ResponseEntity<Void> delete(Long id, Authentication user) {
        columnHolderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}