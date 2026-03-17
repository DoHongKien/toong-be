package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassOrderResponseDto {
    private Long id;
    private String orderCode;
    private Long passId;
    private String passTitle;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
}
