package com.coigniez.resumebuilder.templates.layouts;

import static org.junit.jupiter.api.Assertions.*;

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

import com.coigniez.resumebuilder.domain.resume.dtos.ResumeDetailResponse;
import com.coigniez.resumebuilder.services.ResumeService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StandardNavyBlueTemplateIntegrationTest {

    @Autowired
    private StandardNavyBlueTemplate standardNavyBlueTemplate;
    @Autowired
    private ResumeService resumeService;

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
    void testGenerate() {
        // Arrange
        String title = "test title";
        
        // Act
        long result = standardNavyBlueTemplate.generate(title);


        // Assert
        ResumeDetailResponse resume = resumeService.get(result);


        assertNotNull(resume);

    }
}
