package com.coigniez.resumebuilder.domain.sectionitem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.services.LayoutService;
import com.coigniez.resumebuilder.services.ResumeService;
import com.coigniez.resumebuilder.services.SectionItemService;
import com.coigniez.resumebuilder.services.SectionService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SectionItemServiceIntegrationTest {

    @Autowired
    private SectionItemService sectionItemService;
    @Autowired
    private SectionItemRepository sectionItemRepository;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private LayoutService layoutService;

    private Authentication testuser;
    private Long sectionId;
    private Map<String, Long> methodIds;

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

        ResumeRequest resumeRequest = ResumeRequest.builder().title("Software Developer").build();

        Long resumeId = resumeService.create(null, resumeRequest);

        sectionId = sectionService.create(resumeId, SectionRequest.builder().title("Education").build());
        
        Long layoutId = layoutService.create(resumeId, LayoutRequest.builder().numberOfColumns(1).build());

        methodIds = layoutService.getLatexMethodsMap(layoutId);
    }


    @Test
    void testCreate() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");
    
        SectionItemRequest request = SectionItemRequest.builder()
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(data)
                .latexMethodId(methodIds.get("textbox"))
                .build();
    
        // Act
        Long sectionItemId = sectionItemService.create(sectionId, request);
        SectionItem sectionItem = sectionItemRepository.findById(sectionItemId).orElseThrow();
    
        // Assert
        assertNotNull(sectionItemId);
        assertNotNull(sectionItem.getId());
        assertNotNull(sectionItem.getData());
        assertEquals(1, sectionItem.getItemOrder());
        assertEquals(SectionItemType.TEXTBOX, sectionItem.getType());
        assertEquals("This is some example text", ((Textbox) sectionItem.getData()).getContent());
    }

    @Test
    void testCreatePicture_WithRealImage() throws IOException {
        // Arrange
        MockMultipartFile file = getPictureFile();
        
        SectionItemRequest request = SectionItemRequest.builder()
                .type(SectionItemType.PICTURE.name())
                .itemOrder(1)
                .data(new HashMap<>())
                .latexMethodId(methodIds.get("pictureitem"))
                .build();
    
    
        // Act
        Long pictureId = sectionItemService.createPicture(sectionId, file, request);
        SectionItem sectionItem = sectionItemRepository.findById(pictureId).orElseThrow();
    
        // Assert
        assertNotNull(pictureId);
        assertNotNull(sectionItem.getId());
        assertEquals(SectionItemType.PICTURE, sectionItem.getType());
        assertEquals(1, sectionItem.getItemOrder());
        assertNotNull(((Picture) sectionItem.getData()).getPath());
        
        // Verify actual image content
        byte[] originalBytes = file.getBytes();
        assertNotNull(originalBytes);
        assertTrue(originalBytes.length > 0);
    }
    
    @Test
    void testDeleteAllBySectionId() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");
        
        SectionItemRequest request1 = SectionItemRequest.builder()
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(data)
                .latexMethodId(methodIds.get("textbox"))
                .build();
                
        sectionItemService.create(sectionId, request1);
        
        Map<String, Object> data2 = new HashMap<>();
        data2.put("content", "This is some example text");
        
        SectionItemRequest request2 = SectionItemRequest.builder()
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(data2)
                .latexMethodId(methodIds.get("textbox"))
                .build();
                
        sectionItemService.create(sectionId, request2);
    
        // Act
        sectionItemService.deleteAllBySectionId(sectionId);
    
        // Assert
        assertEquals(0, sectionItemRepository.count());
    }

    // Helper method to get test image
    private MockMultipartFile getPictureFile() throws IOException {
        // Load test image from resources
        Path resourcePath = Paths.get("src", "test", "resources", "blue.jpg");
        byte[] content = Files.readAllBytes(resourcePath);
        
        return new MockMultipartFile(
                "file",
                "blue.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                content
        );
    }
        
}
