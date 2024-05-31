package com.example.block2.dto;

import com.example.block2.enums.UserReportType;
import lombok.Data;

@Data
public class UserFilterDto {
    private Long roleId;
    private String username;
    private String email;
    private Integer page;
    private Integer size;
    private UserReportType reportType;
}
