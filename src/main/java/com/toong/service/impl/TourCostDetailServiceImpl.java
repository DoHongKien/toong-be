package com.toong.service.impl;

import com.toong.modal.dto.TourCostDetailRequestDto;
import com.toong.modal.dto.TourCostDetailResponseDto;
import com.toong.modal.entity.Tour;
import com.toong.modal.entity.TourCostDetail;
import com.toong.repository.TourCostDetailRepository;
import com.toong.repository.TourRepository;
import com.toong.service.TourCostDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourCostDetailServiceImpl implements TourCostDetailService {

    private final TourCostDetailRepository tourCostDetailRepository;
    private final TourRepository tourRepository;

    @Override
    public List<TourCostDetailResponseDto> getCostDetailsByTourId(Long tourId) {
        return tourCostDetailRepository.findByTour_IdOrderBySortOrderAsc(tourId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public TourCostDetailResponseDto createCostDetail(TourCostDetailRequestDto request) {
        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found: " + request.getTourId()));
        TourCostDetail detail = TourCostDetail.builder()
                .tour(tour)
                .isIncluded(request.getIsIncluded())
                .content(request.getContent())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .build();
        return toDto(tourCostDetailRepository.save(detail));
    }

    @Override
    public TourCostDetailResponseDto updateCostDetail(Long id, TourCostDetailRequestDto request) {
        TourCostDetail detail = tourCostDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TourCostDetail not found: " + id));
        if (request.getTourId() != null) {
            Tour tour = tourRepository.findById(request.getTourId())
                    .orElseThrow(() -> new RuntimeException("Tour not found: " + request.getTourId()));
            detail.setTour(tour);
        }
        if (request.getIsIncluded() != null) detail.setIsIncluded(request.getIsIncluded());
        if (request.getContent() != null) detail.setContent(request.getContent());
        if (request.getSortOrder() != null) detail.setSortOrder(request.getSortOrder());
        return toDto(tourCostDetailRepository.save(detail));
    }

    @Override
    public void deleteCostDetail(Long id) {
        if (!tourCostDetailRepository.existsById(id)) {
            throw new RuntimeException("TourCostDetail not found: " + id);
        }
        tourCostDetailRepository.deleteById(id);
    }

    private TourCostDetailResponseDto toDto(TourCostDetail d) {
        return TourCostDetailResponseDto.builder()
                .id(d.getId())
                .tourId(d.getTour() != null ? d.getTour().getId() : null)
                .tourName(d.getTour() != null ? d.getTour().getName() : null)
                .isIncluded(d.getIsIncluded())
                .content(d.getContent())
                .sortOrder(d.getSortOrder())
                .build();
    }
}
