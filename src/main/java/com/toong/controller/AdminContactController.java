package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.ContactMessageResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;
import com.toong.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/contact-messages")
@RequiredArgsConstructor
public class AdminContactController {

    private final ContactMessageService contactMessageService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<ContactMessageResponseDto>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(ApiResponse.success(
                contactMessageService.getMessages(status, page, limit)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ContactMessageResponseDto>> updateStatus(
            @PathVariable Long id, @RequestBody UpdateStatusDto dto) {
        return ResponseEntity.ok(ApiResponse.success(contactMessageService.updateStatus(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.success("Tin nhắn đã được xóa."));
    }
}
