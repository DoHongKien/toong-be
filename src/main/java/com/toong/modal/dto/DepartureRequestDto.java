package com.toong.modal.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DepartureRequestDto {
    private Long tourId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate depositDeadline;
    private LocalDate paymentDeadline;
    private java.math.BigDecimal price;
    private Integer totalSlots;
}
