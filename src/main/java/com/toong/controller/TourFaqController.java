package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.TourFaqResponseDto;
import com.toong.service.TourFaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
public class TourFaqController {

    private final TourFaqService tourFaqService;

    /**
     * GET /api/v1/tours/{tourId}/faqs
     * Lấy danh sách FAQ theo tour (PUBLIC) — dùng trong màn Tour Detail
     */
    @GetMapping("/{tourId}/faqs")
    public ResponseEntity<ApiResponse<List<TourFaqResponseDto>>> getFaqsByTour(
            @PathVariable Long tourId) {
        return ResponseEntity.ok(ApiResponse.success(tourFaqService.getFaqsByTourId(tourId)));
    }
}
