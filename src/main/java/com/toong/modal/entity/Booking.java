package com.toong.modal.entity;

import com.toong.modal.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String bookingCode;

    @ManyToOne
    @JoinColumn(name = "departure_id")
    private Departure departure;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private Integer quantity;

    private BigDecimal totalAmount;

    private BigDecimal depositAmount;

    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String status;

    private Timestamp createdAt;
}