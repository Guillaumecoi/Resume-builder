package com.coigniez.resumebuilder.model.resume.resume;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.model.common.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("resume")
@RequiredArgsConstructor
@Tag(name = "Resume")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ResumeRequest request, Authentication user) {
        return ResponseEntity.ok(resumeService.create(request, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResume(@PathVariable Long id, Authentication user) {
        log.debug("Getting resume with id: " + id);
        try {
            ResumeResponse resume = resumeService.get(id, user);
            return ResponseEntity.ok(resume);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody ResumeRequest request, Authentication user) {
        try {
            resumeService.update(id, request, user);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication user) {
        try {
            resumeService.delete(id, user);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    public ResponseEntity<PageResponse<ResumeResponse>> getAll(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "order", defaultValue = "lastModifiedDate", required = false) String order,
            Authentication connectedUser) {
        return ResponseEntity.ok(resumeService.getAll(page, size, order, connectedUser));
    }

    @PostMapping(value = "/{id}/uploadPicture", consumes = "multipart/form-data")
    public ResponseEntity<Void> uploadPicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication connectedUser) {
        resumeService.uploadPicture(id, file, connectedUser);
        return ResponseEntity.accepted().build();
    }
}
