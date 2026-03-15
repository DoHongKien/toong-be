package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.BookingRequestDto;
import com.toong.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/tours/{idOrSlug}/departures")
    public ResponseEntity<ApiResponse<?>> getDepartures(@PathVariable("idOrSlug") String idOrSlug) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getDepartures(idOrSlug)));
    }

    @PostMapping("/bookings")
    public ResponseEntity<ApiResponse<?>> createBooking(@RequestBody BookingRequestDto request) {
        String bookingCode = bookingService.createBooking(request);
        return ResponseEntity.ok(ApiResponse.success("Booking created successfully", bookingCode));
    }
}