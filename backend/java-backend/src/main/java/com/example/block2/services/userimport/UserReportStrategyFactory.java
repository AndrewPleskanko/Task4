package com.example.block2.services.userimport;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.block2.enums.UserReportType;
import com.example.block2.exceptions.UnsupportedUserReportTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserReportStrategyFactory {

    private final List<UserReportStrategy> strategies;

    public UserReportStrategy lookup(UserReportType reportType) {
        return strategies.stream()
                .filter(strategy -> reportType.equals(strategy.getType()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Unsupported user report type: {}", reportType);
                    throw new UnsupportedUserReportTypeException(reportType);
                });
    }
}
