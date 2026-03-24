package com.toong.modal.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourDetailResponseDto {
    private Long id;
    private String name;
    private String slug;
    private String cardImage;
    private String badge;
    private String region;
    private Integer durationDays;
    private Integer durationNights;
    private String difficulty;
    private String summary;
    private Double basePrice;

    private List<TourCostDetailResponseDto> costDetails;
    private List<TourLuggageResponseDto> luggages;
    private List<TourFaqResponseDto> faqs;
}
