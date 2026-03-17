package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.ContactMessageResponseDto;
import com.toong.modal.dto.ContactRequestDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;
import com.toong.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageService contactMessageService;

    /** PUBLIC: Gửi tin nhắn liên hệ */
    @PostMapping("/api/v1/contact")
    public ResponseEntity<ApiResponse<String>> submit(@RequestBody ContactRequestDto request) {
        contactMessageService.createMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tin nhắn đã được gửi."));
    }

    /** ADMIN: Danh sách hộp thư */
    @GetMapping("/api/v1/admin/contact-messages")
    public ResponseEntity<ApiResponse<PaginationResponse<ContactMessageResponseDto>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(ApiResponse.success(
                contactMessageService.getMessages(status, page, limit)));
    }

    @PatchMapping("/api/v1/admin/contact-messages/{id}/status")
    public ResponseEntity<ApiResponse<ContactMessageResponseDto>> updateStatus(
            @PathVariable Long id, @RequestBody UpdateStatusDto dto) {
        return ResponseEntity.ok(ApiResponse.success(contactMessageService.updateStatus(id, dto)));
    }

    @DeleteMapping("/api/v1/admin/contact-messages/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.success("Tin nhắn đã được xóa."));
    }
}
