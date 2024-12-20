package com.coigniez.resumebuilder.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.common.PageResponse;
import com.coigniez.resumebuilder.domain.resume.ResumeDetailResponse;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.resume.ResumeResponse;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.ResumeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

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
public class ResumeController implements CrudController<ResumeRequest, ResumeDetailResponse> {

    private final ResumeService resumeService;

    @Override
    @PostMapping
    @Operation(operationId = "createResume")
    public ResponseEntity<Long> create(@Valid @RequestBody ResumeRequest request, Authentication user) {
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
    public ResponseEntity<ResumeDetailResponse> get(@PathVariable Long id, Authentication user) {
        ResumeDetailResponse resume = resumeService.get(id);
        return ResponseEntity.ok(resume);
    }

    @Override
    @PostMapping("/{id}")
    @Operation(operationId = "updateResume")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody ResumeRequest request, Authentication user) {
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
    public ResponseEntity<PageResponse<ResumeResponse>> getAllResumes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "order", defaultValue = "lastModifiedDate", required = false) String order,
            Authentication connectedUser) {
        return ResponseEntity.ok(resumeService.getAll(page, size, order));
    }

    @PostMapping(value = "/{id}/uploadPicture", consumes = "multipart/form-data")
    public ResponseEntity<Void> uploadResumePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication connectedUser) {
        resumeService.uploadPicture(id, file);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllResumes(Authentication user) {
        resumeService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}