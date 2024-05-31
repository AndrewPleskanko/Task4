package com.example.block2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserReportDto {
    private byte[] content;
    private String fileName;
}
