package com.toong.modal.dto;

import com.toong.modal.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassOrderRequestDto {

    private Long passId;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private PaymentMethod paymentMethod;

}