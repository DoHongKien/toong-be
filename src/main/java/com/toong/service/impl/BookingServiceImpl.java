package com.toong.service.impl;

import com.toong.event.NotificationEvent;
import com.toong.modal.dto.BookingRequestDto;
import com.toong.modal.dto.DepartureResponseDto;
import com.toong.modal.entity.Booking;
import com.toong.modal.entity.Departure;
import com.toong.modal.enums.NotifType;
import com.toong.repository.BookingRepository;
import com.toong.repository.DepartureRepository;
import com.toong.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final DepartureRepository departureRepository;
    private final BookingRepository bookingRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<DepartureResponseDto> getDepartures(String tourIdentifier) {
        List<Departure> departures;
        
        try {
            Long tourId = Long.parseLong(tourIdentifier);
            departures = departureRepository.findByTourId(tourId);
        } catch (NumberFormatException e) {
            // If not a number, treat as slug
            departures = departureRepository.findByTourSlug(tourIdentifier);
        }

        return departures.stream().map(d -> {
            Integer available = d.getTotalSlots() - d.getBookedSlots();
            return DepartureResponseDto.builder()
                    .id(d.getId() != null ? Long.valueOf(d.getId()) : null)
                    .startDate(d.getStartDate())
                    .endDate(d.getEndDate())
                    .depositDeadline(d.getDepositDeadline())
                    .paymentDeadline(d.getPaymentDeadline())
                    .price(d.getPrice())
                    .availableSlots(available)
                    .status(d.getStatus())
                    .build();
        }).toList();
    }

    @Override
    @Transactional
    public String createBooking(BookingRequestDto request) {

        Departure departure = departureRepository.findById(request.getDepartureId())
                .orElseThrow(() -> new RuntimeException("Departure not found"));

        int available = departure.getTotalSlots() - departure.getBookedSlots();

        if (request.getQuantity() > available) {
            throw new RuntimeException("Not enough slots");
        }

        BigDecimal price = departure.getPrice();

        BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(request.getQuantity()));

        BigDecimal deposit = totalAmount.multiply(BigDecimal.valueOf(0.3));

        BigDecimal remaining = totalAmount.subtract(deposit);

        String bookingCode = "BK" + System.currentTimeMillis();

        Booking booking = new Booking();

        booking.setBookingCode(bookingCode);
        booking.setDeparture(departure);
        booking.setFirstName(request.getFirstName());
        booking.setLastName(request.getLastName());
        booking.setPhone(request.getPhone());
        booking.setEmail(request.getEmail());
        booking.setQuantity(request.getQuantity());
        booking.setPaymentMethod(request.getPaymentMethod());
        booking.setTotalAmount(totalAmount);
        booking.setDepositAmount(deposit);
        booking.setRemainingAmount(remaining);

        Booking saved = bookingRepository.save(booking);

        departure.setBookedSlots(departure.getBookedSlots() + request.getQuantity());
        departureRepository.save(departure);

        // Push notification
        String tourName = departure.getTour() != null ? departure.getTour().getName() : "";
        String desc = String.format("%s %s đặt tour %s - %d người",
                request.getFirstName(), request.getLastName(), tourName, request.getQuantity());
        eventPublisher.publishEvent(new NotificationEvent(
                this, NotifType.booking,
                "Booking mới " + bookingCode,
                desc,
                saved.getId(),
                "/cms/bookings"
        ));

        return bookingCode;
    }
}