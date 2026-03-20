package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.TourLuggageRequestDto;
import com.toong.modal.dto.TourLuggageResponseDto;
import com.toong.service.TourLuggageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/tour-luggages")
@RequiredArgsConstructor
public class AdminTourLuggageController {

    private final TourLuggageService tourLuggageService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TourLuggageResponseDto>>> getAll(
            @RequestParam(name = "tourId") Long tourId) {
        List<TourLuggageResponseDto> result = tourLuggageService.getLuggagesByTourId(tourId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TourLuggageResponseDto>> create(
            @RequestBody TourLuggageRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(tourLuggageService.createLuggage(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourLuggageResponseDto>> update(
            @PathVariable Long id,
            @RequestBody TourLuggageRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(tourLuggageService.updateLuggage(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        tourLuggageService.deleteLuggage(id);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa."));
    }
}
