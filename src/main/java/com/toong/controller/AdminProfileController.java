package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.ChangePasswordRequestDto;
import com.toong.modal.dto.EmployeeResponseDto;
import com.toong.modal.dto.ProfileUpdateRequestDto;
import com.toong.security.CustomUserDetails;
import com.toong.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                profileService.getProfile(userDetails.getUsername())));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ProfileUpdateRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(
                profileService.updateProfile(userDetails.getUsername(), request)));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChangePasswordRequestDto request) {
        profileService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công."));
    }
}
