package com.toong.modal.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AdventurePassRequestDto {
    private String title;
    private String subtitle;
    private BigDecimal price;
    private LocalDate validityDate;
    private Boolean isSignature;
    private String colorTheme;
}
