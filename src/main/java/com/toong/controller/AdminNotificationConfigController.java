package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.NotificationConfigRequestDto;
import com.toong.modal.dto.NotificationConfigResponseDto;
import com.toong.modal.dto.NotificationConfigStatusDto;
import com.toong.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/notification-configs")
@RequiredArgsConstructor
public class AdminNotificationConfigController {

    private final NotificationService notificationService;

    // 14.4 – Lấy danh sách cấu hình routing
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationConfigResponseDto>>> getAllConfigs() {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getAllConfigs()));
    }

    // 14.5 – Tạo cấu hình mới
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationConfigResponseDto>> createConfig(
            @RequestBody NotificationConfigRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(notificationService.createConfig(request)));
    }

    // 14.6 – Cập nhật cấu hình
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationConfigResponseDto>> updateConfig(
            @PathVariable Long id,
            @RequestBody NotificationConfigRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.updateConfig(id, request)));
    }

    // 14.7 – Bật / Tắt cấu hình
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<NotificationConfigResponseDto>> updateStatus(
            @PathVariable Long id,
            @RequestBody NotificationConfigStatusDto dto) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.updateConfigStatus(id, dto)));
    }

    // 14.8 – Xóa cấu hình
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteConfig(@PathVariable Long id) {
        notificationService.deleteConfig(id);
        return ResponseEntity.ok(ApiResponse.success("Cấu hình đã được xóa."));
    }
}
