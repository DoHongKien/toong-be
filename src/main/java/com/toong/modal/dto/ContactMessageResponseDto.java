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
public class ContactMessageResponseDto {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}
