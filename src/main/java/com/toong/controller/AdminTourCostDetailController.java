package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.TourCostDetailRequestDto;
import com.toong.modal.dto.TourCostDetailResponseDto;
import com.toong.service.TourCostDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/tour-cost-details")
@RequiredArgsConstructor
public class AdminTourCostDetailController {

    private final TourCostDetailService tourCostDetailService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TourCostDetailResponseDto>>> getAll(
            @RequestParam(name = "tourId") Long tourId) {
        List<TourCostDetailResponseDto> result = tourCostDetailService.getCostDetailsByTourId(tourId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TourCostDetailResponseDto>> create(
            @RequestBody TourCostDetailRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(tourCostDetailService.createCostDetail(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourCostDetailResponseDto>> update(
            @PathVariable Long id,
            @RequestBody TourCostDetailRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(tourCostDetailService.updateCostDetail(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        tourCostDetailService.deleteCostDetail(id);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa."));
    }
}
