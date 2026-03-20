package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.NotificationListResponseDto;
import com.toong.security.CustomUserDetails;
import com.toong.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/notifications")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final NotificationService notificationService;

    // 14.1 – Lấy danh sách thông báo của user hiện tại
    @GetMapping
    public ResponseEntity<ApiResponse<NotificationListResponseDto>> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "false") boolean unreadOnly) {
        Long employeeId = userDetails.getEmployee().getId();
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getNotifications(employeeId, limit, unreadOnly)));
    }

    // 14.2 – Đánh dấu 1 thông báo đã đọc
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        notificationService.markAsRead(id, userDetails.getEmployee().getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 14.3 – Đánh dấu tất cả thông báo đã đọc
    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAllAsRead(userDetails.getEmployee().getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
