package com.toong.modal.entity;

import com.toong.modal.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pass_orders")
public class PassOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;

    @ManyToOne
    @JoinColumn(name = "pass_id")
    private AdventurePass pass;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String status;

    private Timestamp createdAt;
}