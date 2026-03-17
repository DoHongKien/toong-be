package com.toong.service;

import com.toong.modal.dto.BookingResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;

public interface AdminBookingService {
    PaginationResponse<BookingResponseDto> getAllBookings(String status, Long departureId, int page, int limit);
    BookingResponseDto updateStatus(Long id, UpdateStatusDto dto);
}
