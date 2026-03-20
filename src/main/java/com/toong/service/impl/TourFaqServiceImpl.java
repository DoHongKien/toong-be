package com.toong.service.impl;

import com.toong.modal.dto.TourFaqRequestDto;
import com.toong.modal.dto.TourFaqResponseDto;
import com.toong.modal.entity.Tour;
import com.toong.modal.entity.TourFaq;
import com.toong.repository.TourFaqRepository;
import com.toong.repository.TourRepository;
import com.toong.service.TourFaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourFaqServiceImpl implements TourFaqService {

    private final TourFaqRepository tourFaqRepository;
    private final TourRepository tourRepository;

    @Override
    public List<TourFaqResponseDto> getFaqsByTourId(Long tourId) {
        return tourFaqRepository.findByTour_IdOrderBySortOrderAsc(tourId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TourFaqResponseDto> getAllFaqs() {
        return tourFaqRepository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public TourFaqResponseDto createFaq(TourFaqRequestDto request) {
        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found: " + request.getTourId()));
        TourFaq faq = TourFaq.builder()
                .tour(tour)
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .build();
        return toDto(tourFaqRepository.save(faq));
    }

    @Override
    public TourFaqResponseDto updateFaq(Long id, TourFaqRequestDto request) {
        TourFaq faq = tourFaqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TourFaq not found: " + id));
        if (request.getTourId() != null) {
            Tour tour = tourRepository.findById(request.getTourId())
                    .orElseThrow(() -> new RuntimeException("Tour not found: " + request.getTourId()));
            faq.setTour(tour);
        }
        if (request.getQuestion() != null) faq.setQuestion(request.getQuestion());
        if (request.getAnswer() != null) faq.setAnswer(request.getAnswer());
        if (request.getSortOrder() != null) faq.setSortOrder(request.getSortOrder());
        return toDto(tourFaqRepository.save(faq));
    }

    @Override
    public void deleteFaq(Long id) {
        if (!tourFaqRepository.existsById(id)) {
            throw new RuntimeException("TourFaq not found: " + id);
        }
        tourFaqRepository.deleteById(id);
    }

    private TourFaqResponseDto toDto(TourFaq f) {
        return TourFaqResponseDto.builder()
                .id(f.getId())
                .tourId(f.getTour() != null ? f.getTour().getId() : null)
                .tourName(f.getTour() != null ? f.getTour().getName() : null)
                .question(f.getQuestion())
                .answer(f.getAnswer())
                .sortOrder(f.getSortOrder())
                .build();
    }
}
