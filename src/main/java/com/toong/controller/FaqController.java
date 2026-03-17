package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.FaqRequestDto;
import com.toong.modal.dto.FaqResponseDto;
import com.toong.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    /** PUBLIC */
    @GetMapping("/api/v1/faqs")
    public ResponseEntity<ApiResponse<List<FaqResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(faqService.getAllFaqs()));
    }

    /** ADMIN */
    @PostMapping("/api/v1/admin/faqs")
    public ResponseEntity<ApiResponse<FaqResponseDto>> create(@RequestBody FaqRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(faqService.createFaq(request)));
    }

    @PutMapping("/api/v1/admin/faqs/{id}")
    public ResponseEntity<ApiResponse<FaqResponseDto>> update(
            @PathVariable Long id, @RequestBody FaqRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(faqService.updateFaq(id, request)));
    }

    @PatchMapping("/api/v1/admin/faqs/{id}")
    public ResponseEntity<ApiResponse<FaqResponseDto>> patch(
            @PathVariable Long id, @RequestBody FaqRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(faqService.updateFaq(id, request)));
    }

    @DeleteMapping("/api/v1/admin/faqs/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        faqService.deleteFaq(id);
        return ResponseEntity.ok(ApiResponse.success("FAQ đã được xóa."));
    }
}
