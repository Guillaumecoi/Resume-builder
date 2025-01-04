package com.coigniez.resumebuilder.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResp;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResp;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutUpdateReq;
import com.coigniez.resumebuilder.interfaces.CrudController;
import com.coigniez.resumebuilder.services.LatexMethodService;
import com.coigniez.resumebuilder.services.LayoutService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("layouts")
@RequiredArgsConstructor
@Tag(name = "Layout")
public class LayoutController
        implements CrudController<LayoutCreateReq, LayoutUpdateReq, LayoutResp, Long> {

    private final LayoutService layoutService;
    private final LatexMethodService latexMethodService;

    @Override
    @Operation(operationId = "createLayout")
    public ResponseEntity<Long> create(@Valid LayoutCreateReq request, Authentication user) {
        Long id = layoutService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @Override
    @Operation(operationId = "getLayout")
    public ResponseEntity<LayoutResp> get(Long id, Authentication user) {
        LayoutResp layout = layoutService.get(id);
        return ResponseEntity.ok(layout);
    }

    @Override
    @Operation(operationId = "updateLayout")
    public ResponseEntity<Void> update(Long id, LayoutUpdateReq request, Authentication user) {
        request.setId(id);
        layoutService.update(request);
        return ResponseEntity.ok().build();
    }

    @Override
    @Operation(operationId = "deleteLayout")
    public ResponseEntity<Void> delete(Long id, Authentication user) {
        layoutService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Generate a PDF file from a layout
     * 
     * @param id layout id to generate the PDF from
     * @return the generated PDF file
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> generateLatexPdf(@PathVariable long id)
            throws IOException, InterruptedException {
        byte[] pdfFile = layoutService.generateLatexPdf(id);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfFile));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=layout_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfFile.length)
                .body(resource);
    }

    /**
     * Get all methods for a layout
     * 
     * @param id layout id to get the methods from
     * @return the methods
     */
    @GetMapping("/{id}/methods")
    public ResponseEntity<List<LatexMethodResp>> getLatexMethods(@PathVariable Long id) {
        return ResponseEntity.ok(latexMethodService.getAllByParentId(id));
    }

}
