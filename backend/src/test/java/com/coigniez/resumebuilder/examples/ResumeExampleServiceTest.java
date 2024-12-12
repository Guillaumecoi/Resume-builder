package com.coigniez.resumebuilder.examples;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.services.LayoutService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ResumeExampleServiceTest {

    @Autowired
    private ResumeExampleService resumeExampleService;
    @Autowired
    private LayoutService layoutService;

    private Authentication testuser;

    @BeforeEach
    void setUp() {
        // Create mock users
        testuser = new UsernamePasswordAuthenticationToken(
                "testuser", 
                "password", 
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(testuser);
    }
    
    @Test
    void testCreateExampleResume() throws IOException, InterruptedException {
        //Act
        List<Object> list = resumeExampleService.createExampleResume("Software Engineer");
        LayoutResponse layout = (LayoutResponse) list.get(1);

        File generatedPdf = layoutService.generateLatexPdf(layout.getId());
    
        // Assert
        assertTrue(generatedPdf.exists(), "The PDF file should be generated.");
        assertTrue(generatedPdf.getName().equals("Software Engineer.pdf"), "The PDF file should have the correct name.");
        assertTrue(generatedPdf.length() > 0, "PDF file should not be empty");
    
        // Use debug flag to inspect the generated PDF before it is deleted or comment out the line below and delete the file manually later
        // Clean up
        Files.deleteIfExists(generatedPdf.toPath());
    }
}
