package com.toong.service;

import com.toong.modal.dto.TourFaqRequestDto;
import com.toong.modal.dto.TourFaqResponseDto;

import java.util.List;

public interface TourFaqService {
    List<TourFaqResponseDto> getFaqsByTourId(Long tourId);
    List<TourFaqResponseDto> getAllFaqs();
    TourFaqResponseDto createFaq(TourFaqRequestDto request);
    TourFaqResponseDto updateFaq(Long id, TourFaqRequestDto request);
    void deleteFaq(Long id);
}
