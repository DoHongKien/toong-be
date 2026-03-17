package com.toong.service.impl;

import com.toong.modal.dto.DashboardStatsDto;
import com.toong.repository.BookingRepository;
import com.toong.repository.ContactMessageRepository;
import com.toong.repository.EmployeeRepository;
import com.toong.repository.TourRepository;
import com.toong.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final EmployeeRepository employeeRepository;
    private final TourRepository tourRepository;

    @Override
    public DashboardStatsDto getStats() {
        long totalBookings = bookingRepository.count();
        long totalTours = tourRepository.count();
        long totalEmployees = employeeRepository.count();
        long newContacts = contactMessageRepository.countByStatus("new");
        long pendingBookings = bookingRepository.countByStatus("pending");

        // Revenue: sum of total_amount for fully_paid bookings
        BigDecimal totalRevenue = bookingRepository.sumTotalAmountByStatus("fully_paid");
        BigDecimal revenueThisMonth = bookingRepository.sumTotalAmountThisMonth("fully_paid");

        // Bookings this month
        long bookingsThisMonth = bookingRepository.countThisMonth();

        return DashboardStatsDto.builder()
                .totalBookings(totalBookings)
                .bookingsThisMonth(bookingsThisMonth)
                .totalRevenue(totalRevenue != null ? totalRevenue.doubleValue() : 0)
                .revenueThisMonth(revenueThisMonth != null ? revenueThisMonth.doubleValue() : 0)
                .pendingBookings(pendingBookings)
                .newContactMessages(newContacts)
                .totalTours(totalTours)
                .totalEmployees(totalEmployees)
                .build();
    }
}
