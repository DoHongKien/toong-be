package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqResponseDto {
    private Long id;
    private String question;
    private String answer;
    private Integer sortOrder;
}
