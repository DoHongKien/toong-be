package com.toong.modal.dto;

import lombok.Data;

@Data
public class ContactRequestDto {
    private String fullName;
    private String phone;
    private String email;
    private String message;
}
