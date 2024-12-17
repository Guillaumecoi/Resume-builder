package com.coigniez.resumebuilder.examples;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
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
        // Act
        List<Object> list = resumeExampleService.createExampleResume("Software Engineer");
        LayoutResponse layout = (LayoutResponse) list.get(1);

        byte[] generatedPdf = layoutService.generateLatexPdf(layout.getId());

        // Assert
        assertNotNull(generatedPdf);
        assertTrue(generatedPdf.length > 0);

        // Feel free to write the PDF to a file to open and inspect it
    }
}
