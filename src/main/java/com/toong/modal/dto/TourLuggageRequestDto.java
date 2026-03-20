package com.toong.modal.dto;

import lombok.Data;

@Data
public class TourLuggageRequestDto {
    private Long tourId;
    private String name;
    private String detail;
    private Integer sortOrder;
}
