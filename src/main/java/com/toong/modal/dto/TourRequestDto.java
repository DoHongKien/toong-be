package com.toong.modal.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TourRequestDto {
    private String name;
    private String slug;
    private String heroImage;
    private String cardImage;
    private String badge;
    private String region;
    private Integer durationDays;
    private Integer durationNights;
    private String difficulty;
    private Integer distanceKm;
    private Integer minAge;
    private Integer maxAge;
    private String summary;
    private String description;
    private BigDecimal basePrice;
}
