package com.toong.modal.dto;

import lombok.Data;

@Data
public class FaqRequestDto {
    private String question;
    private String answer;
    private Integer sortOrder;
}
