package com.toong.modal.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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

    /** Optional — có thể null hoặc rỗng. Backend check != null && !isEmpty() trước khi insert. */
    private List<CostDetailItem> costDetails;

    /** Optional — có thể null hoặc rỗng. */
    private List<LuggageItem> luggages;

    /** Optional — có thể null hoặc rỗng. */
    private List<FaqItem> faqs;

    @Data
    public static class CostDetailItem {
        private Boolean isIncluded;
        private String content;
        private Integer sortOrder;
    }

    @Data
    public static class LuggageItem {
        private String name;
        private String detail;
        private Integer sortOrder;
    }

    @Data
    public static class FaqItem {
        private String question;
        private String answer;
        private Integer sortOrder;
    }
}
