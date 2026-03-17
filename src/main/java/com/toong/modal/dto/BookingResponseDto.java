package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private String bookingCode;
    private Long departureId;
    private String tourName;
    private String startDate;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Integer quantity;
    private BigDecimal totalAmount;
    private BigDecimal depositAmount;
    private BigDecimal remainingAmount;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
}
