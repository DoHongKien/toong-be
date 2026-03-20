package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourCostDetailResponseDto {
    private Long id;
    private Long tourId;
    private String tourName;
    private Boolean isIncluded;
    private String content;
    private Integer sortOrder;
}
