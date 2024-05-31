package com.example.block2.services.userimport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.block2.entity.User;
import com.example.block2.enums.UserReportType;

@SpringBootTest
class ExcelReportStrategyTest {

    @Autowired
    private ExcelUserReportStrategy excelUserReportStrategy;


    @Test
    public void generateReport_WithValidUsers_ReturnsNonEmptyReport() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("test1");
        user1.setEmail("test1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("test2");
        user2.setEmail("test2@example.com");

        List<User> users = Arrays.asList(user1, user2);

        // When
        byte[] report = excelUserReportStrategy.generateReport(users);

        // Then
        assertNotNull(report);
        assertTrue(report.length > 0);
    }

    @Test
    public void getType_WhenCalled_ReturnsExcel() {
        // Given & When
        UserReportType reportType = excelUserReportStrategy.getType();

        // Then
        assertEquals(UserReportType.EXCEL, reportType);
    }

    @Test
    public void getFileExtension_WhenCalled_ReturnsXlsx() {
        // Given & When
        String fileExtension = excelUserReportStrategy.getFileExtension();

        // Then
        assertEquals(".xlsx", fileExtension);
    }
}