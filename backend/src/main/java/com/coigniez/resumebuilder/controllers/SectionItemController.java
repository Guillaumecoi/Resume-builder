package com.coigniez.resumebuilder.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.SectionItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("section-items")
@RequiredArgsConstructor
public class SectionItemController
        implements CrudController<CreateSectionItemRequest, UpdateSectionItemRequest, SectionItemResponse, Long> {

    private final SectionItemService sectionItemService;
    private final ObjectMapper objectMapper;

    @Override
    @Operation(operationId = "createSectionItem")
    public ResponseEntity<Long> create(@Valid CreateSectionItemRequest request, Authentication user) {
        Long id = sectionItemService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    @Operation(operationId = "getSectionItem")
    public ResponseEntity<SectionItemResponse> get(Long id, Authentication user) {
        SectionItemResponse sectionItem = sectionItemService.get(id);
        return ResponseEntity.ok(sectionItem);
    }

    @Override
    @Operation(operationId = "updateSectionItem")
    public ResponseEntity<Void> update(Long id, UpdateSectionItemRequest request, Authentication user) {
        request.setId(id);
        sectionItemService.update(request);
        return ResponseEntity.ok().build();
    }

    @Override
    @Operation(operationId = "deleteSectionItem")
    public ResponseEntity<Void> delete(Long id, Authentication user) {
        sectionItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPicture(
            @PathVariable Long sectionId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("request") String requestJson) throws JsonMappingException, JsonProcessingException {

        CreateSectionItemRequest request = objectMapper.readValue(requestJson, CreateSectionItemRequest.class);
        request.setSectionId(sectionId);
        return ResponseEntity.ok(sectionItemService.createPicture(file, request));
    }
}