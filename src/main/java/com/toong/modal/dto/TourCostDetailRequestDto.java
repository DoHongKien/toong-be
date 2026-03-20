package com.toong.modal.dto;

import lombok.Data;

@Data
public class TourCostDetailRequestDto {
    private Long tourId;
    private Boolean isIncluded;
    private String content;
    private Integer sortOrder;
}
