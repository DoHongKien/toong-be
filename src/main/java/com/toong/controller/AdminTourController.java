package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.TourDetailResponseDto;
import com.toong.modal.dto.TourRequestDto;
import com.toong.modal.dto.TourResponseDto;
import com.toong.service.AdminTourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/tours")
@RequiredArgsConstructor
public class AdminTourController {

    private final AdminTourService adminTourService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<TourResponseDto>>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(adminTourService.getAllTours(name, page, limit)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TourDetailResponseDto>> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(adminTourService.getTourDetail(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TourResponseDto>> create(@RequestBody TourRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(adminTourService.createTour(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourResponseDto>> update(
            @PathVariable Long id, @RequestBody TourRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(adminTourService.updateTour(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        adminTourService.deleteTour(id);
        return ResponseEntity.ok(ApiResponse.success("Tour đã được xóa."));
    }
}
