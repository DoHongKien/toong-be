package com.toong.repository;

import com.toong.modal.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByBookingCode(String bookingCode);

    long countByStatus(String status);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = :status")
    BigDecimal sumTotalAmountByStatus(@Param("status") String status);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = :status " +
           "AND MONTH(b.createdAt) = MONTH(CURRENT_DATE) AND YEAR(b.createdAt) = YEAR(CURRENT_DATE)")
    BigDecimal sumTotalAmountThisMonth(@Param("status") String status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE MONTH(b.createdAt) = MONTH(CURRENT_DATE) " +
           "AND YEAR(b.createdAt) = YEAR(CURRENT_DATE)")
    long countThisMonth();
}