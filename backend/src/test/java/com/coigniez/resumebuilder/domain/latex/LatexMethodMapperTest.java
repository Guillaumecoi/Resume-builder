package com.coigniez.resumebuilder.domain.latex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.UpdateLatexMethodRequest;

@SpringBootTest
@ActiveProfiles("test")
public class LatexMethodMapperTest {

    @Autowired
    private LatexMethodMapper mapper;

    @Test
    void testToDto() {
        // Arrange
        LatexMethod entity = LatexMethod.builder()
                .id(1L)
                .name("Method1")
                .method("MethodContent")
                .type(HasLatexMethod.TEXTBOX)
                .build();

        // Act
        LatexMethodResponse dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getMethod(), dto.getMethod());
        assertEquals(entity.getType(), dto.getType());
    }

    @Test
    void testToEntity() {
        // Arrange
        CreateLatexMethodRequest request = CreateLatexMethodRequest.builder()
                .name("Method1")
                .method("MethodContent")
                .type(HasLatexMethod.TEXTBOX)
                .build();

        // Act
        LatexMethod entity = mapper.toEntity(request);

        // Assert
        assertNotNull(entity);
        assertEquals(request.getName(), entity.getName());
        assertEquals(request.getMethod(), entity.getMethod());
        assertEquals(request.getType(), entity.getType());
    }

    @Test
    void testUpdateEntity() {
        // Arrange
        UpdateLatexMethodRequest request = UpdateLatexMethodRequest.builder()
                .id(1L)
                .name("UpdatedMethod")
                .method("UpdatedContent")
                .build();

        LatexMethod entity = LatexMethod.builder()
                .id(1L)
                .name("Method1")
                .method("MethodContent")
                .type(HasLatexMethod.TEXTBOX)
                .build();

        // Act
        mapper.updateEntity(entity, request);

        // Assert
        assertEquals(1L, entity.getId(), "ID should not be updated");
        assertEquals(request.getName(), entity.getName());
        assertEquals(request.getMethod(), entity.getMethod());
    }
}