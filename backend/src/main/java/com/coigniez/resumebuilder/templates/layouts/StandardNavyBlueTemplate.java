package com.coigniez.resumebuilder.templates.layouts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.resume.dtos.CreateResumeRequest;
import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemCreateReq;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Title;
import com.coigniez.resumebuilder.services.ColumnSectionService;
import com.coigniez.resumebuilder.services.LayoutService;
import com.coigniez.resumebuilder.services.ResumeService;
import com.coigniez.resumebuilder.services.SectionItemService;
import com.coigniez.resumebuilder.services.SectionService;
import com.coigniez.resumebuilder.templates.color.ColorTemplates;
import com.coigniez.resumebuilder.templates.methods.LatexMethodTemplates;

@Component
public class StandardNavyBlueTemplate {

    public static final String LAYOUT_NAME = "Standard Navy Blue";
    public static final String LAYOUT_DESCRIPTION = "A simple, clean layout with a navy blue color scheme.";
    public static final byte[] IMAGE_CONTENT;

    static {
        byte[] content = new byte[0];
        try {
            Path resourcePath = Paths.get("src", "main", "resources", "images", "ali-mammadli-unsplash.jpg");
            content = Files.readAllBytes(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IMAGE_CONTENT = content;
    }

    private final ResumeService resumeService;
    private final LayoutService layoutService;
    private final SectionService sectionService;
    private final SectionItemService sectionItemService;
    private final ColumnSectionService columnSectionService;

    @Lazy
    public StandardNavyBlueTemplate(ResumeService resumeService, LayoutService layoutService,
            SectionService sectionService, SectionItemService sectionItemService,
            ColumnSectionService columnSectionService) {
        this.resumeService = resumeService;
        this.layoutService = layoutService;
        this.sectionService = sectionService;
        this.sectionItemService = sectionItemService;
        this.columnSectionService = columnSectionService;
    }

    long resumeId;
    long sectionTitleMethodId;

    public long generate(String title) {
        resumeId = createResume(title);
        long layoutId = createLayout(resumeId);
        LayoutResponse layout = layoutService.get(layoutId);
        long leftColumnId = layout.getColumns().get(0).getId();
        long rightColumnId = layout.getColumns().get(1).getId();
        Map<Class<?>, List<LatexMethodResponse>> methodIds = layoutService.getLatexMethodsMap(layoutId);
        sectionTitleMethodId = methodIds.get(ColumnSection.class).get(0).getId();

        // Left Column Sections
        addPictureSection(leftColumnId, 1);

        // Right Column Sections
        addTitleSection(rightColumnId, 1);

        return layoutId;
    }

    private long createResume(String title) {
        CreateResumeRequest request = CreateResumeRequest.builder()
                .title(title)
                .build();
        return resumeService.create(request);

    }

    private long createLayout(long resumeId) {
        CreateLayoutRequest request = CreateLayoutRequest.builder()
                .resumeId(resumeId)
                .numberOfColumns(2)
                .columns(List.of(
                        CreateColumnRequest.builder()
                                .columnNumber(1)
                                .backgroundColor(ColorLocation.DARK_BG)
                                .textColor(ColorLocation.LIGHT_TEXT)
                                .borderColor(ColorLocation.ACCENT)
                                .borderRight(2.5)
                                .build(),
                        CreateColumnRequest.builder()
                                .columnNumber(2)
                                .backgroundColor(ColorLocation.LIGHT_BG)
                                .textColor(ColorLocation.DARK_TEXT)
                                .borderColor(ColorLocation.ACCENT)
                                .build()))
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .latexMethods(LatexMethodTemplates.getStandardMethods())
                .build();

        return layoutService.create(request);
    }

    private void addPictureSection(Long columnId, int sectionOrder) {
        Long sectionId = sectionService.create(CreateSectionRequest.builder()
                .resumeId(resumeId)
                .title("Picture")
                .showTitle(false)
                .build());

        SectionItemCreateReq pictureRequest = SectionItemCreateReq.builder()
                .sectionId(sectionId)
                .item(Picture.builder()
                        .caption("Photo by Ali Mammadli on Unsplash")
                        .width(0.9)
                        .height(1.1)
                        .shadow(2.0)
                        .zoom(1.8)
                        .yoffset(-8.0)
                        .build())
                .build();

        //Todo: handle exception
        Path resourcePath = Paths.get("src", "main", "resources", "images", "ali-mammadli-unsplash.jpg");
        byte[] content;
        try {
                content = Files.readAllBytes(resourcePath);
        } catch (IOException e) {
                e.printStackTrace();
                content = new byte[0];
        }

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "ali-mammadli-unsplash.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                content);
        sectionItemService.createPicture(file, pictureRequest);

        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .latexMethodId(sectionTitleMethodId)
                .itemOrder(sectionOrder)
                .build());
    }

    private void addTitleSection(Long columnId, int sectionOrder) {
        SectionItemCreateReq title = SectionItemCreateReq.builder()
                .item(Title.builder()
                        .title("John Doe")
                        .subtitle("Software Developer")
                        .build())
                .build();
            
        Long titleId = sectionService.create(CreateSectionRequest.builder()
                .resumeId(resumeId)
                .title("Title")
                .showTitle(false)
                .sectionItems(List.of(title))
                .build());
        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(titleId)
                .latexMethodId(sectionTitleMethodId)
                .itemOrder(sectionOrder)
                .endsep(6.0)
                .build());
    }

}
