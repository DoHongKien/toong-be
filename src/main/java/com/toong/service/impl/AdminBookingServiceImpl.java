package com.toong.service.impl;

import com.toong.modal.dto.BookingResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;
import com.toong.modal.entity.Booking;
import com.toong.modal.entity.Departure;
import com.toong.repository.BookingRepository;
import com.toong.service.AdminBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminBookingServiceImpl implements AdminBookingService {

    private final BookingRepository bookingRepository;

    @Override
    public PaginationResponse<BookingResponseDto> getAllBookings(String status, Long departureId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Booking> bookingPage = bookingRepository.findAll(pageable);

        var data = bookingPage.getContent().stream()
                .filter(b -> status == null || status.equals(b.getStatus()))
                .filter(b -> departureId == null || (b.getDeparture() != null && b.getDeparture().getId().equals(departureId)))
                .map(this::toDto)
                .collect(Collectors.toList());

        return PaginationResponse.<BookingResponseDto>builder()
                .data(data)
                .pagination(PaginationResponse.PaginationMeta.builder()
                        .page(page).limit(limit).total(bookingPage.getTotalElements()).build())
                .build();
    }

    @Override
    public BookingResponseDto updateStatus(Long id, UpdateStatusDto dto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(dto.getStatus());
        return toDto(bookingRepository.save(booking));
    }

    private BookingResponseDto toDto(Booking b) {
        Departure d = b.getDeparture();
        String tourName = (d != null && d.getTour() != null) ? d.getTour().getName() : null;
        String startDate = (d != null && d.getStartDate() != null) ? d.getStartDate().toString() : null;
        Long depId = d != null ? d.getId() : null;

        return BookingResponseDto.builder()
                .id(b.getId())
                .bookingCode(b.getBookingCode()).departureId(depId).tourName(tourName)
                .startDate(startDate).firstName(b.getFirstName()).lastName(b.getLastName())
                .phone(b.getPhone()).email(b.getEmail()).quantity(b.getQuantity())
                .totalAmount(b.getTotalAmount()).depositAmount(b.getDepositAmount())
                .remainingAmount(b.getRemainingAmount())
                .paymentMethod(b.getPaymentMethod() != null ? b.getPaymentMethod().name() : null)
                .status(b.getStatus())
                .build();
    }
}
