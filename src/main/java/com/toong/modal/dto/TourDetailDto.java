package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourDetailDto {
    private Long id;
    private String name;
    private String slug;
    private String heroImage;
    private String cardImage;
    private String badge;
    private String region;
    private Integer durationDays;
    private Integer durationNights;
    private String difficulty;
    private String summary;
    private String description;
    private Double basePrice;
    private Integer distanceKm;
    private Integer minAge;
    private Integer maxAge;

    private List<ItineraryDto> itineraries;
    private List<TourCostDetailDto> costDetails;
    private List<TourLuggageDto> luggages;
    private List<TourFaqDto> faqs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItineraryDto {
        private Integer id;
        private Integer dayNumber;
        private String title;
        private String description;
        private List<ItineraryTimelineDto> timelines;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItineraryTimelineDto {
        private Integer id;
        private String executionTime;
        private String activity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TourCostDetailDto {
        private Integer id;
        private Boolean isIncluded;
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TourLuggageDto {
        private Integer id;
        private String name;
        private String detail;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TourFaqDto {
        private Integer id;
        private String question;
        private String answer;
    }
}
