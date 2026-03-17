package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.BookingResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;
import com.toong.service.AdminBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<BookingResponseDto>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long departureId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                adminBookingService.getAllBookings(status, departureId, page, limit)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponseDto>> updateStatus(
            @PathVariable Long id, @RequestBody UpdateStatusDto dto) {
        return ResponseEntity.ok(ApiResponse.success(adminBookingService.updateStatus(id, dto)));
    }
}
