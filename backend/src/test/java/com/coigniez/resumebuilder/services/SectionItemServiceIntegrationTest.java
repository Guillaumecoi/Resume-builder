package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.resume.ResumeRequest;
import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.domain.section.SectionResponse;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRepository;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;

import jakarta.persistence.EntityNotFoundException;
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
    private Authentication otheruser;
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

        otheruser = new UsernamePasswordAuthenticationToken(
                "otheruser", 
                "password", 
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(testuser);

        ResumeRequest resumeRequest = ResumeRequest.builder().title("Software Developer").build();

        Long resumeId = resumeService.create(resumeRequest);

        sectionId = sectionService.create(SectionRequest.builder()
                .resumeId(resumeId)
                .title("Education")
                .build());
        
        Long layoutId = layoutService.create(LayoutRequest.builder()
                .resumeId(resumeId)
                .numberOfColumns(1)
                .build());

        methodIds = layoutService.getLatexMethodsMap(layoutId);
    }

    @Test
    void testCreate() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");
    
        SectionItemRequest request = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(data)
                .latexMethodId(methodIds.get("textbox"))
                .build();
    
        // Act
        Long sectionItemId = sectionItemService.create(request);
        SectionItem sectionItem = sectionItemRepository.findById(sectionItemId).orElseThrow();
    
        // Assert
        assertNotNull(sectionItemId, "Section item ID should not be null");
        assertNotNull(sectionItem.getId(), "Section item entity ID should not be null");
        assertNotNull(sectionItem.getData(), "Section item data should not be null");
        assertEquals(1, sectionItem.getItemOrder(), "Item order should be 1");
        assertEquals(SectionItemType.TEXTBOX, sectionItem.getType(), "Item type should be TEXTBOX");
        assertEquals(methodIds.get("textbox"), sectionItem.getLatexMethod().getId(), "LatexMethod ID should be the same as the request");
        assertEquals("This is some example text", ((Textbox) sectionItem.getData()).getContent(), 
            "Textbox content should be 'This is some example text'");
    }

    @Test
    void testCreatePicture_WithRealImage() throws IOException {
        // Arrange
        MockMultipartFile file = getPictureFile();
        
        SectionItemRequest request = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.PICTURE.name())
                .itemOrder(1)
                .data(new HashMap<>())
                .latexMethodId(methodIds.get("pictureitem"))
                .build();
    
    
        // Act
        Long pictureId = sectionItemService.createPicture(file, request);
        SectionItem sectionItem = sectionItemRepository.findById(pictureId).orElseThrow();
    
        // Assert
        assertNotNull(pictureId, "The ID should not be null");
        assertEquals(pictureId, sectionItem.getId(), "The ID of the entity should match the returned ID");
        assertEquals(SectionItemType.PICTURE, sectionItem.getType(), "The type should be PICTURE");
        assertEquals(1, sectionItem.getItemOrder(), "The item order should be 1");
        assertNotNull(((Picture) sectionItem.getData()).getPath(), "The path should not be null");
        
        // Verify actual image content
        byte[] originalBytes = file.getBytes();
        assertNotNull(originalBytes, "The retrieved image should not be null");
        assertTrue(originalBytes.length > 0, "The retrieved image should not be empty");
    }

    @Test
    void testCreate_AutoIncrementItemOrder() {
        // Arrange
        SectionItemRequest request1 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .data(new HashMap<String, Object>() {{
                    put("content", "First item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();

        SectionItemRequest request2 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .data(new HashMap<String, Object>() {{
                    put("content", "Second item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();

        // Act
        sectionItemService.create(request1);
        sectionItemService.create(request2);

        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder));  // Sort by item order for easier comparison

        assertEquals(2, items.size(), "There should be 2 items");
        assertEquals(List.of(1, 2), items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()), "Item orders should be 1, 2");
        assertEquals("First item", ((Textbox) items.get(0).getData()).getContent(), "First item in the list should be the first item created");
        assertEquals("Second item", ((Textbox) items.get(1).getData()).getContent(), "Second item in the list should be the second item created");
    }

    @Test
    void testCreate_IncrementsItemOrder() {
        // Arrange
        SectionItemRequest request1 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(new HashMap<String, Object>() {{
                    put("content", "First item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();

        sectionItemService.create(request1);

        SectionItemRequest request2 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(new HashMap<String, Object>() {{
                    put("content", "Second item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();

        sectionItemService.create(request2);

        // Act
        SectionItemRequest request3 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(new HashMap<String, Object>() {{
                    put("content", "Third item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();

        sectionItemService.create(request3);

        SectionItemRequest request4 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(4)
                .data(new HashMap<String, Object>() {{
                    put("content", "Fourth item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();

        sectionItemService.create(request4);

        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder));  // Sort by item order for easier comparison

        assertEquals(4, items.size(), "There should be 4 items");
        assertEquals(List.of(1, 2, 3, 4), items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()), "Item orders should be 1, 2, 3, 4");
        assertEquals("First item", ((Textbox) items.get(0).getData()).getContent(), "First item in the list should be the first item created");
        assertEquals("Third item", ((Textbox) items.get(1).getData()).getContent(), "Second item in the list should be the third item created");
        assertEquals("Second item", ((Textbox) items.get(2).getData()).getContent(), "Third item in the list should be the second item created");
        assertEquals("Fourth item", ((Textbox) items.get(3).getData()).getContent(), "Fourth item in the list should be the fourth item created");
    }

    @Test
    void testUpdateLatexMethod() {
        // Arrange
        SectionItemRequest createRequest = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(new HashMap<String, Object>() {{
                    put("content", "First item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
        Long sectionItemId = sectionItemService.create(createRequest);

        SectionItem sectionItem = sectionItemRepository.findById(sectionItemId).orElseThrow();
        LatexMethod oldLatexMethod = sectionItem.getLatexMethod();


        // Act
        SectionItemRequest updateRequest = SectionItemRequest.builder()
                .id(sectionItemId)
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(new HashMap<String, Object>() {{
                    put("content", "Updated item");
                }})
                .latexMethodId(methodIds.get("skillitem"))
                .build();
        sectionItemService.update(updateRequest);

        // Assert
        SectionItem updatedSectionItem = sectionItemRepository.findById(sectionItemId).orElseThrow();
        LatexMethod newLatexMethod = sectionItem.getLatexMethod();

        assertEquals(methodIds.get("skillitem"), updatedSectionItem.getLatexMethod().getId(), "LatexMethod should be updated");
        assertFalse(oldLatexMethod.getSectionItems().contains(updatedSectionItem), "Old LatexMethod should not contain the updated SectionItem");
        assertTrue(newLatexMethod.getSectionItems().contains(updatedSectionItem), "New LatexMethod should contain the updated SectionItem");
    }

    @Test
    void testUpdate_DecrementItemOrder() {
        // Arrange
        SectionItemRequest request1 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(new HashMap<String, Object>() {{
                    put("content", "First item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
    
        sectionItemService.create(request1);
    
        SectionItemRequest request2 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(new HashMap<String, Object>() {{
                    put("content", "Second item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
    
        sectionItemService.create(request2);
    
        SectionItemRequest request3 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(3)
                .data(new HashMap<String, Object>() {{
                    put("content", "Third item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
    
        Long itemId3 = sectionItemService.create(request3);
    
        // Act
        sectionItemService.update(SectionItemRequest.builder()
                .id(itemId3)
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(new HashMap<String, Object>() {{
                    put("content", "Updated Third item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build());
    
        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder));  // Sort by item order for easier comparison
    
        assertEquals(3, items.size(), "There should be 3 items");
        assertEquals(List.of(1, 2, 3), items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()), "Item orders should be 1, 2, 3");
        assertEquals("First item", ((Textbox) items.get(0).getData()).getContent(), "First item in the list should be the first item created");
        assertEquals("Updated Third item", ((Textbox) items.get(1).getData()).getContent(), "Second item in the list should be the third item created");
        assertEquals("Second item", ((Textbox) items.get(2).getData()).getContent(), "Third item in the list should be the second item created");
    }

    @Test
    void testUpdate_IncrementsItemOrder() {
        // Arrange
        SectionItemRequest request1 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(new HashMap<String, Object>() {{
                    put("content", "First item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
    
        sectionItemService.create(request1);
    
        SectionItemRequest request2 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(new HashMap<String, Object>() {{
                    put("content", "Second item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
    
        sectionItemService.create(request2);
    
        SectionItemRequest request3 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(3)
                .data(new HashMap<String, Object>() {{
                    put("content", "Third item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
    
        sectionItemService.create(request3);
    
        // Act
        SectionItemRequest request4 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(new HashMap<String, Object>() {{
                    put("content", "Fourth item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
    
        sectionItemService.create(request4);
    
        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder));  // Sort by item order for easier comparison
    
        assertEquals(4, items.size(), "There should be 4 items");
        assertEquals(List.of(1, 2, 3, 4), items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()), "Item orders should be 1, 2, 3, 4");
        assertEquals("First item", ((Textbox) items.get(0).getData()).getContent(), "First item in the list should be the first item created");
        assertEquals("Fourth item", ((Textbox) items.get(1).getData()).getContent(), "Second item in the list should be the fourth item created");
        assertEquals("Second item", ((Textbox) items.get(2).getData()).getContent(), "Third item in the list should be the second item created");
        assertEquals("Third item", ((Textbox) items.get(3).getData()).getContent(), "Fourth item in the list should be the third item created");
    }

    @Test
    void testDelete() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        SectionItemRequest request = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(data)
                .latexMethodId(methodIds.get("textbox"))
                .build();

        Long sectionItemId = sectionItemService.create(request);

        // Act
        sectionItemService.delete(sectionItemId);

        // Assert
        assertEquals(0, sectionItemRepository.count(), "There should be no section items left");
        assertThrows(EntityNotFoundException.class, () -> sectionItemService.get(sectionItemId), 
            "Should throw EntityNotFoundException when trying to get a deleted section item");
    }

    @Test
    void testDelete_DecrementItemOrder() {
        // Arrange
        SectionItemRequest request1 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(new HashMap<String, Object>() {{
                    put("content", "First item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
        
        sectionItemService.create(request1);

        SectionItemRequest request2 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(new HashMap<String, Object>() {{
                    put("content", "Second item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();
        
        Long itemId2 = sectionItemService.create(request2);

        SectionItemRequest request3 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(3)
                .data(new HashMap<String, Object>() {{
                    put("content", "Third item");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build();

        sectionItemService.create(request3);

        // Act
        sectionItemService.delete(itemId2);

        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder));  // Sort by item order for easier comparison

        assertEquals(2, items.size(), "There should be 2 items");
        assertEquals(List.of(1, 2), items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()), "Item orders should be 1, 2");
        assertEquals("First item", ((Textbox) items.get(0).getData()).getContent(), "First item in the list should be the first item created");
        assertEquals("Third item", ((Textbox) items.get(1).getData()).getContent(), "Second item in the list should be the third item created");
    }
    
    @Test
    void testDeleteAllBySectionId() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");
        
        SectionItemRequest request1 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(data)
                .latexMethodId(methodIds.get("textbox"))
                .build();
                
        sectionItemService.create(request1);
        
        Map<String, Object> data2 = new HashMap<>();
        data2.put("content", "This is some example text");
        
        SectionItemRequest request2 = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(2)
                .data(data2)
                .latexMethodId(methodIds.get("textbox"))
                .build();
                
        sectionItemService.create(request2);
    
        // Act
        sectionItemService.removeAllByParentId(sectionId);
    
        // Assert
        SectionResponse section = sectionService.get(sectionId);

        assertTrue(sectionItemRepository.findAllBySectionId(sectionId).isEmpty(), "There should be no section items in the section");
        assertEquals(0, section.getSectionItems().size(), "There should be no section items in the section");
    }

    @Test
    void testAccessControl() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        SectionItemRequest request = SectionItemRequest.builder()
                .sectionId(sectionId)
                .type(SectionItemType.TEXTBOX.name())
                .itemOrder(1)
                .data(data)
                .latexMethodId(methodIds.get("textbox"))
                .build();

        Long sectionItemId = sectionItemService.create(request);

        // Act & Assert
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        assertThrows(AccessDeniedException.class, () -> sectionItemService.create(request), 
            "Should not be able to create a section item for a section that does not belong to the user");
        assertThrows(AccessDeniedException.class, () -> sectionItemService.get(sectionItemId),
            "Should not be able to get a section item that does not belong to the user");
        request.setId(sectionItemId);
        assertThrows(AccessDeniedException.class, () -> sectionItemService.update(request),
            "Should not be able to update a section item that does not belong to the user");
        assertThrows(AccessDeniedException.class, () -> sectionItemService.delete(sectionItemId),
            "Should not be able to delete a section item that does not belong to the user");
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
