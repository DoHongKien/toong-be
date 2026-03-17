package com.toong.modal.dto;

import lombok.Data;

@Data
public class EmployeeRequestDto {
    private Long roleId;
    private String username;
    private String password;
    private String fullName;
    private String email;
}
