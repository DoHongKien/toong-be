package com.toong.modal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private long totalBookings;
    private long bookingsThisMonth;
    private double totalRevenue;
    private double revenueThisMonth;
    private long pendingBookings;
    private long newContactMessages;
    private long totalTours;
    private long totalEmployees;
}
