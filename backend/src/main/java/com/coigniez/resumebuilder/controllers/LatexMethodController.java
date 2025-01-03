package com.coigniez.resumebuilder.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.latex.dtos.UpdateLatexMethodRequest;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.LatexMethodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("latexmethods")
@RequiredArgsConstructor
@Tag(name = "LatexMethod")
public class LatexMethodController
        implements CrudController<CreateLatexMethodRequest, UpdateLatexMethodRequest, LatexMethodResponse, Long> {

    private final LatexMethodService latexMethodService;

    @Override
    @Operation(operationId = "createLatexMethod")
    public ResponseEntity<Long> create(@Valid CreateLatexMethodRequest request, Authentication user) {
        Long id = latexMethodService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    @Operation(operationId = "getLatexMethod")
    public ResponseEntity<LatexMethodResponse> get(Long id, Authentication user) {
        LatexMethodResponse latexMethod = latexMethodService.get(id);
        return ResponseEntity.ok(latexMethod);
    }

    @Override
    @Operation(operationId = "updateLatexMethod")
    public ResponseEntity<Void> update(Long id, UpdateLatexMethodRequest request, Authentication user) {
        request.setId(id);
        latexMethodService.update(request);
        return ResponseEntity.ok().build();
    }

    @Override
    @Operation(operationId = "deleteLatexMethod")
    public ResponseEntity<Void> delete(Long id, Authentication user) {
        latexMethodService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
