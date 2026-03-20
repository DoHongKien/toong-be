package com.toong.service;

import com.toong.modal.dto.ChangePasswordRequestDto;
import com.toong.modal.dto.EmployeeResponseDto;
import com.toong.modal.dto.ProfileUpdateRequestDto;

public interface ProfileService {
    EmployeeResponseDto getProfile(String username);
    EmployeeResponseDto updateProfile(String username, ProfileUpdateRequestDto request);
    void changePassword(String username, ChangePasswordRequestDto request);
}
