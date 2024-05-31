package com.example.block2.services.userimport;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.block2.entity.User;
import com.example.block2.enums.UserReportType;
import com.example.block2.exceptions.UnsupportedUserReportTypeException;
import com.example.block2.exceptions.UserReportGenerationException;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * Strategy for generating reports in CSV format.
 */
@Slf4j
@Component
public class CsvUserReportStrategy implements UserReportStrategy {

    /**
     * Generates a report in CSV format for a list of users.
     *
     * @param users the list of users to include in the report
     * @return the generated report as a byte array
     * @throws UnsupportedUserReportTypeException if an error occurs while generating the report
     */
    @Override
    public byte[] generateReport(List<User> users) {
        log.info("Starting CSV report generation");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteArrayOutputStream);

        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            for (User user : users) {
                String[] line = {user.getId().toString(), user.getUsername(), user.getEmail()};
                csvWriter.writeNext(line);
            }
        } catch (Exception e) {
            log.error("Error while generating CSV user report", e);
            throw new UserReportGenerationException(UserReportType.CSV);
        }

        log.debug("CSV users report generated successfully");
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public UserReportType getType() {
        return UserReportType.CSV;
    }

    @Override
    public String getFileExtension() {
        return ".csv";
    }
}
