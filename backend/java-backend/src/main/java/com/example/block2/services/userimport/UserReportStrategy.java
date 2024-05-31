package com.example.block2.services.userimport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.block2.entity.User;
import com.example.block2.enums.UserReportType;

public interface UserReportStrategy {

    byte[] generateReport(List<User> users);

    UserReportType getType();

    String getFileExtension();

    default String generateReportName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT);
        String dateString = dateFormat.format(new Date());

        return "Users_report_" + dateString + getFileExtension();
    }
}
