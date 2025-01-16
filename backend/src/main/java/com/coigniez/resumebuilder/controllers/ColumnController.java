package com.coigniez.resumebuilder.controllers;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnCreateReq;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnUpdateReq;
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
public class ColumnController implements CrudController<ColumnCreateReq, ColumnUpdateReq, ColumnResp, Long> {

    private final ColumnService columnService;

    @Override
    @Operation(operationId = "createColumn")
    public ResponseEntity<Long> create(@Valid ColumnCreateReq request, Authentication user) {
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
    public ResponseEntity<ColumnResp> get(Long id, Authentication user) {
        ColumnResp column = columnService.get(id);
        return ResponseEntity.ok(column);
    }

    @Override
    @Operation(operationId = "updateColumn")
    public ResponseEntity<Void> update(Long id, ColumnUpdateReq request, Authentication user) {
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

    @PostMapping(value = "/createwithpicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "createPictureColumn")
    public ResponseEntity<Long> createPicture(
            @RequestParam("file") MultipartFile file,
            @RequestPart("request") ColumnCreateReq request) {

        Long id = columnService.CreateWithPicture(file, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(id);
    }
}
