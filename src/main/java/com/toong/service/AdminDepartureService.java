package com.toong.service;

import com.toong.modal.dto.DepartureRequestDto;
import com.toong.modal.dto.DepartureResponseDto;

public interface AdminDepartureService {
    DepartureResponseDto createDeparture(DepartureRequestDto request);
    DepartureResponseDto updateDeparture(Long id, DepartureRequestDto request);
    void deleteDeparture(Long id);
}
