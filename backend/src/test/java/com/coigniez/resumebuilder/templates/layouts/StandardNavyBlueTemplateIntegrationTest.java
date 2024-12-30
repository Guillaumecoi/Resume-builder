package com.coigniez.resumebuilder.templates.layouts;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResponse;
import com.coigniez.resumebuilder.services.LayoutService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StandardNavyBlueTemplateIntegrationTest {

    @Autowired
    private StandardNavyBlueTemplate standardNavyBlueTemplate;
    @Autowired
    private LayoutService layoutService;

    private Authentication testuser;

    @BeforeEach
    void setUp() {
        // Create mock users
        testuser = new UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));


        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(testuser);
    }
    
    @Test
    void testGenerate() throws IOException, InterruptedException {
        // Arrange
        String title = "test title";
        
        // Act
        long layoutId = standardNavyBlueTemplate.generate(title);
        LayoutResponse layout = layoutService.get(layoutId);
        byte[] generatedPdf = layoutService.generateLatexPdf(layoutId);



        // Assert
        assertNotNull(layoutId);
        assertNotNull(layout);
        assertNotNull(generatedPdf);
        assertTrue(generatedPdf.length > 0);

        // Feel free to write the PDF to a file to open and inspect it
        Files.write(Paths.get("test.pdf"), generatedPdf);

    }
}
