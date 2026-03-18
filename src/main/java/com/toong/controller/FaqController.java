package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.FaqResponseDto;
import com.toong.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FaqResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(faqService.getAllFaqs()));
    }
}
