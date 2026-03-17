package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartureResponseDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate depositDeadline;
    private LocalDate paymentDeadline;
    private BigDecimal price;
    private Integer availableSlots;
    private String status;
}