package com.toong.service.impl;

import com.toong.modal.dto.TourDetailDto;
import com.toong.modal.dto.TourResponseDto;
import com.toong.modal.entity.Tour;
import com.toong.repository.TourRepository;
import com.toong.service.TourService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;

    public TourServiceImpl(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    @Override
    public List<TourResponseDto> getAllTours(String region, String difficulty, Integer durationDays) {
        List<Tour> tours = tourRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (region != null && !region.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("region"), region));
            }
            if (difficulty != null && !difficulty.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("difficulty"), difficulty));
            }
            if (durationDays != null) {
                predicates.add(criteriaBuilder.equal(root.get("durationDays"), durationDays));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        
        return tours.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    private TourResponseDto convertToResponseDto(Tour tour) {
        return TourResponseDto.builder()
                .id(tour.getId())
                .name(tour.getName())
                .slug(tour.getSlug())
                .cardImage(tour.getCardImage())
                .badge(tour.getBadge())
                .region(tour.getRegion())
                .durationDays(tour.getDurationDays())
                .durationNights(tour.getDurationNights())
                .difficulty(tour.getDifficulty())
                .summary(tour.getSummary())
                .basePrice(tour.getBasePrice())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TourDetailDto getTourBySlug(String slug) {
        Tour tour = tourRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Tour not found with slug: " + slug));
        return convertToDetailDto(tour);
    }

    private TourDetailDto convertToDetailDto(Tour tour) {
        return TourDetailDto.builder()
                .id(tour.getId())
                .name(tour.getName())
                .slug(tour.getSlug())
                .heroImage(tour.getHeroImage())
                .cardImage(tour.getCardImage())
                .badge(tour.getBadge())
                .region(tour.getRegion())
                .durationDays(tour.getDurationDays())
                .durationNights(tour.getDurationNights())
                .difficulty(tour.getDifficulty())
                .summary(tour.getSummary())
                .description(tour.getDescription())
                .basePrice(tour.getBasePrice())
                .distanceKm(tour.getDistanceKm())
                .minAge(tour.getMinAge())
                .maxAge(tour.getMaxAge())
                .itineraries(tour.getItineraries() != null ? tour.getItineraries().stream()
                        .map(it -> TourDetailDto.ItineraryDto.builder()
                                .id(it.getId())
                                .dayNumber(it.getDayNumber())
                                .title(it.getTitle())
                                .description(it.getDescription())
                                .timelines(it.getTimelines() != null ? it.getTimelines().stream()
                                        .map(tl -> TourDetailDto.ItineraryTimelineDto.builder()
                                                .id(tl.getId())
                                                .executionTime(tl.getExecutionTime() != null ? tl.getExecutionTime().toString() : null)
                                                .activity(tl.getActivity())
                                                .build())
                                        .collect(Collectors.toList()) : new ArrayList<>())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .costDetails(tour.getCostDetails() != null ? tour.getCostDetails().stream()
                        .map(cd -> TourDetailDto.TourCostDetailDto.builder()
                                .id(cd.getId())
                                .isIncluded(cd.getIsIncluded())
                                .content(cd.getContent())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .luggages(tour.getLuggages() != null ? tour.getLuggages().stream()
                        .map(lg -> TourDetailDto.TourLuggageDto.builder()
                                .id(lg.getId())
                                .name(lg.getName())
                                .detail(lg.getDetail())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .faqs(tour.getFaqs() != null ? tour.getFaqs().stream()
                        .map(fq -> TourDetailDto.TourFaqDto.builder()
                                .id(fq.getId())
                                .question(fq.getQuestion())
                                .answer(fq.getAnswer())
                                .build())
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
