package com.toong.service;

import com.toong.modal.dto.TourDetailDto;
import com.toong.modal.dto.TourResponseDto;
import java.util.List;

public interface TourService {
    List<TourResponseDto> getAllTours(String region, String difficulty, Integer durationDays);

    TourDetailDto getTourBySlug(String slug);
}
