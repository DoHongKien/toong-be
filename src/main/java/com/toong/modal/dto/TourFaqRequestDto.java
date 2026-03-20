package com.toong.modal.dto;

import lombok.Data;

@Data
public class TourFaqRequestDto {
    private Long tourId;
    private String question;
    private String answer;
    private Integer sortOrder;
}
