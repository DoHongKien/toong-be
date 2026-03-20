package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.TourFaqRequestDto;
import com.toong.modal.dto.TourFaqResponseDto;
import com.toong.service.TourFaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/tour-faqs")
@RequiredArgsConstructor
public class AdminTourFaqController {

    private final TourFaqService tourFaqService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TourFaqResponseDto>>> getAll(
            @RequestParam(name = "tourId") Long tourId) {
        List<TourFaqResponseDto> result = tourFaqService.getFaqsByTourId(tourId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TourFaqResponseDto>> create(
            @RequestBody TourFaqRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(tourFaqService.createFaq(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourFaqResponseDto>> update(
            @PathVariable Long id,
            @RequestBody TourFaqRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(tourFaqService.updateFaq(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TourFaqResponseDto>> patch(
            @PathVariable Long id,
            @RequestBody TourFaqRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(tourFaqService.updateFaq(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        tourFaqService.deleteFaq(id);
        return ResponseEntity.ok(ApiResponse.success("TourFAQ đã được xóa."));
    }
}
