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
public class EmployeeResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String status;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private RoleResponseDto role;
}
