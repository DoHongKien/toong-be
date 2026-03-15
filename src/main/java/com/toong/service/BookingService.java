package com.toong.service;

import com.toong.modal.dto.BookingRequestDto;
import com.toong.modal.dto.DepartureResponseDto;

import java.util.List;

public interface BookingService {

    List<DepartureResponseDto> getDepartures(String tourIdentifier);

    String createBooking(BookingRequestDto request);

}