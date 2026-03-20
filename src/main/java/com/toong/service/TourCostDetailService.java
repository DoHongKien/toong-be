package com.toong.service;

import com.toong.modal.dto.TourCostDetailRequestDto;
import com.toong.modal.dto.TourCostDetailResponseDto;

import java.util.List;

public interface TourCostDetailService {
    List<TourCostDetailResponseDto> getCostDetailsByTourId(Long tourId);
    TourCostDetailResponseDto createCostDetail(TourCostDetailRequestDto request);
    TourCostDetailResponseDto updateCostDetail(Long id, TourCostDetailRequestDto request);
    void deleteCostDetail(Long id);
}
