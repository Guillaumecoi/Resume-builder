package com.coigniez.resumebuilder.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.services.SectionItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("section-items")
@RequiredArgsConstructor
public class SectionItemController {

    private final SectionItemService sectionItemService;
    private final ObjectMapper objectMapper;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPicture(
            @PathVariable Long sectionId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("request") String requestJson) throws JsonMappingException, JsonProcessingException {
        
        SectionItemRequest request = objectMapper.readValue(requestJson, SectionItemRequest.class);
        request.setSectionId(sectionId);
        return ResponseEntity.ok(sectionItemService.createPicture(file, request));
    }
}