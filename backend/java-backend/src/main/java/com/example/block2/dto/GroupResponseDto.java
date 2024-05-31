package com.example.block2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"content", "totalPages"})
public class GroupResponseDto<T> {
    private List<T> content;
    private int totalPages;

    public GroupResponseDto(List<T> content, int totalPages) {
        this.content = content;
        this.totalPages = totalPages;
    }
}