package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.TourDetailDto;
import com.toong.modal.dto.TourResponseDto;
import com.toong.modal.entity.Tour;
import com.toong.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tours")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TourResponseDto>>> getAllTours(
            @RequestParam(required = false, name = "region") String region,
            @RequestParam(required = false, name = "difficulty") String difficulty,
            @RequestParam(required = false, name = "duration_days") Integer durationDays
    ) {
        List<TourResponseDto> tours = tourService.getAllTours(region, difficulty, durationDays);
        return ResponseEntity.ok(ApiResponse.success(tours));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<TourDetailDto>> getTourBySlug(@PathVariable("slug") String slug) {
        TourDetailDto tour = tourService.getTourBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(tour));
    }
}
