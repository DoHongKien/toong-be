package com.toong.service;

import com.toong.modal.dto.PassOrderResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.TourRequestDto;
import com.toong.modal.dto.TourResponseDto;
import com.toong.modal.dto.UpdateStatusDto;

public interface AdminTourService {
    PaginationResponse<TourResponseDto> getAllTours(String name, int page, int limit);
    TourResponseDto createTour(TourRequestDto request);
    TourResponseDto updateTour(Long id, TourRequestDto request);
    void deleteTour(Long id);
    PaginationResponse<PassOrderResponseDto> getAllPassOrders(String status, int page, int limit);
    PassOrderResponseDto updatePassOrderStatus(Long id, UpdateStatusDto dto);
}
