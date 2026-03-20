package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourLuggageResponseDto {
    private Long id;
    private Long tourId;
    private String tourName;
    private String name;
    private String detail;
    private Integer sortOrder;
}
