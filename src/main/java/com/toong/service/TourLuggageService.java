package com.toong.service;

import com.toong.modal.dto.TourLuggageRequestDto;
import com.toong.modal.dto.TourLuggageResponseDto;

import java.util.List;

public interface TourLuggageService {
    List<TourLuggageResponseDto> getLuggagesByTourId(Long tourId);
    TourLuggageResponseDto createLuggage(TourLuggageRequestDto request);
    TourLuggageResponseDto updateLuggage(Long id, TourLuggageRequestDto request);
    void deleteLuggage(Long id);
}
