package com.example.block2.services.userimport;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.example.block2.entity.User;
import com.example.block2.enums.UserReportType;
import com.example.block2.exceptions.UnsupportedUserReportTypeException;
import com.example.block2.exceptions.UserReportGenerationException;
import lombok.extern.slf4j.Slf4j;

/**
 * Strategy for generating reports in Excel format.
 */
@Slf4j
@Component
public class ExcelUserReportStrategy implements UserReportStrategy {

    /**
     * Generates a report in Excel format for a list of users.
     *
     * @param users the list of users to include in the report
     * @return the generated report as a byte array
     * @throws UnsupportedUserReportTypeException if an error occurs while generating the report
     */
    @Override
    public byte[] generateReport(List<User> users) {
        log.info("Generating Excel report for {} users", users.size());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Username");
            headerRow.createCell(2).setCellValue("Email");

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getEmail());
            }

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("Error while generating EXCEL user report", e);
            throw new UserReportGenerationException(UserReportType.EXCEL);
        }
        log.debug("Excel report generated successfully");
        return outputStream.toByteArray();
    }

    @Override
    public UserReportType getType() {
        return UserReportType.EXCEL;
    }

    @Override
    public String getFileExtension() {
        return ".xlsx";
    }
}