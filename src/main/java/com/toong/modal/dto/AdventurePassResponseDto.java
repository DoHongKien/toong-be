package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdventurePassResponseDto {
    private Long id;
    private String title;
    private String subtitle;
    private BigDecimal price;
    private LocalDate validityDate;
    private Boolean isSignature;
    private String colorTheme;
    private List<PassFeatureDto> features;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassFeatureDto {
        private String content;
        private Boolean isBold;
    }
}
