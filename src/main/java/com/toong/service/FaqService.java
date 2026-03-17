package com.toong.service;

import com.toong.modal.dto.FaqRequestDto;
import com.toong.modal.dto.FaqResponseDto;

import java.util.List;

public interface FaqService {
    List<FaqResponseDto> getAllFaqs();
    FaqResponseDto createFaq(FaqRequestDto request);
    FaqResponseDto updateFaq(Long id, FaqRequestDto request);
    void deleteFaq(Long id);
}
