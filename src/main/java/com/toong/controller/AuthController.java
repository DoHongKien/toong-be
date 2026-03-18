package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.EncodePasswordDto;
import com.toong.modal.dto.LoginRequestDto;
import com.toong.modal.dto.LoginResponseDto;
import com.toong.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Utility endpoint — CHỈ dùng khi dev để gen BCrypt hash cho SQL seed.
     * Nên xóa hoặc bảo vệ kỹ trước khi deploy production.
     *
     * POST /api/v1/admin/auth/encode-password
     * Body: { "password": "Admin@123" }
     */
    @PostMapping("/encode-password")
    public ResponseEntity<ApiResponse<String>> encodePassword(@RequestBody EncodePasswordDto request) {
        String encoded = authService.encodePassword(request.getPassword());
        return ResponseEntity.ok(ApiResponse.success(encoded));
    }
}
