package com.toong.modal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departures")
public class Departure  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate depositDeadline;

    private LocalDate paymentDeadline;

    private BigDecimal price;

    private Integer totalSlots;

    private Integer bookedSlots;

    private String status;
}