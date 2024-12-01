package com.coigniez.resumebuilder.examples;

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

import com.coigniez.resumebuilder.model.layout.LayoutDTO;
import com.coigniez.resumebuilder.model.resume.ResumeDetailResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ResumeExampleServiceTest {

    @Autowired
    private ResumeExampleService resumeExampleService;

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
    void testCreateExampleResume() throws JsonProcessingException {
        //Act
        List<Object> list = resumeExampleService.createExampleResume("Software Engineer 2");
        ResumeDetailResponse resume = (ResumeDetailResponse) list.get(0);
        LayoutDTO layout = (LayoutDTO) list.get(1);

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resume));
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(layout));
    }
}
