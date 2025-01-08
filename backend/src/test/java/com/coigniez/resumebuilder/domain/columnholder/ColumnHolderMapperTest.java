package com.coigniez.resumebuilder.domain.columnholder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderResp;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.HeaderFooter;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderFooterCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderFooterResp;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageResp;

@SpringBootTest
public class ColumnHolderMapperTest {

    @Autowired
    private ColumnHolderMapper columnHolderMapper;

    @Test
    void testToEntity_HeaderFooterCreateReq() {
        HeaderFooterCreateReq request = HeaderFooterCreateReq.builder()
                .height(10.0)
                .repeatOnEveryPage(true)
                .build();

        ColumnHolder entity = columnHolderMapper.toEntity(request);

        assertNotNull(entity);
        assertTrue(entity instanceof HeaderFooter);
        HeaderFooter headerFooter = (HeaderFooter) entity;
        assertEquals(10.0, headerFooter.getHeight());
        assertTrue(headerFooter.getRepeatOnEveryPage());
        assertEquals(0, headerFooter.getColumns().size());
    }

    @Test
    void testToEntity_PageCreateReq() {
        PageCreateReq request = PageCreateReq.builder()
                .pageNumber(1)
                .build();

        ColumnHolder entity = columnHolderMapper.toEntity(request);

        assertNotNull(entity);
        assertTrue(entity instanceof LayoutPage);
        LayoutPage layoutPage = (LayoutPage) entity;
        assertEquals(1, layoutPage.getPageNumber());
    }

    @Test
    void testToDto_HeaderFooter() {
        HeaderFooter entity = HeaderFooter.builder()
                .height(10.0)
                .repeatOnEveryPage(true)
                .build();

        ColumnHolderResp dto = columnHolderMapper.toDto(entity);

        assertNotNull(dto);
        assertTrue(dto instanceof HeaderFooterResp);
        HeaderFooterResp headerFooterResp = (HeaderFooterResp) dto;
        assertEquals(10.0, headerFooterResp.getHeight());
        assertTrue(headerFooterResp.getRepeatOnEveryPage());
    }

    @Test
    void testToDto_LayoutPage() {
        LayoutPage entity = LayoutPage.builder()
                .pageNumber(1)
                .build();

        ColumnHolderResp dto = columnHolderMapper.toDto(entity);

        assertNotNull(dto);
        assertTrue(dto instanceof PageResp);
        PageResp pageResp = (PageResp) dto;
        assertEquals(1, pageResp.getPageNumber());
    }
}