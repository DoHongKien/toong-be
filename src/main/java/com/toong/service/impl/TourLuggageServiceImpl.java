package com.toong.service.impl;

import com.toong.modal.dto.TourLuggageRequestDto;
import com.toong.modal.dto.TourLuggageResponseDto;
import com.toong.modal.entity.Tour;
import com.toong.modal.entity.TourLuggage;
import com.toong.repository.TourLuggageRepository;
import com.toong.repository.TourRepository;
import com.toong.service.TourLuggageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourLuggageServiceImpl implements TourLuggageService {

    private final TourLuggageRepository tourLuggageRepository;
    private final TourRepository tourRepository;

    @Override
    public List<TourLuggageResponseDto> getLuggagesByTourId(Long tourId) {
        return tourLuggageRepository.findByTour_IdOrderBySortOrderAsc(tourId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public TourLuggageResponseDto createLuggage(TourLuggageRequestDto request) {
        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found: " + request.getTourId()));
        TourLuggage luggage = TourLuggage.builder()
                .tour(tour)
                .name(request.getName())
                .detail(request.getDetail())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .build();
        return toDto(tourLuggageRepository.save(luggage));
    }

    @Override
    public TourLuggageResponseDto updateLuggage(Long id, TourLuggageRequestDto request) {
        TourLuggage luggage = tourLuggageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TourLuggage not found: " + id));
        if (request.getTourId() != null) {
            Tour tour = tourRepository.findById(request.getTourId())
                    .orElseThrow(() -> new RuntimeException("Tour not found: " + request.getTourId()));
            luggage.setTour(tour);
        }
        if (request.getName() != null) luggage.setName(request.getName());
        if (request.getDetail() != null) luggage.setDetail(request.getDetail());
        if (request.getSortOrder() != null) luggage.setSortOrder(request.getSortOrder());
        return toDto(tourLuggageRepository.save(luggage));
    }

    @Override
    public void deleteLuggage(Long id) {
        if (!tourLuggageRepository.existsById(id)) {
            throw new RuntimeException("TourLuggage not found: " + id);
        }
        tourLuggageRepository.deleteById(id);
    }

    private TourLuggageResponseDto toDto(TourLuggage l) {
        return TourLuggageResponseDto.builder()
                .id(l.getId())
                .tourId(l.getTour() != null ? l.getTour().getId() : null)
                .tourName(l.getTour() != null ? l.getTour().getName() : null)
                .name(l.getName())
                .detail(l.getDetail())
                .sortOrder(l.getSortOrder())
                .build();
    }
}
