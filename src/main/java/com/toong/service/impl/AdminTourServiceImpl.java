package com.toong.service.impl;

import com.toong.modal.dto.*;
import com.toong.modal.entity.*;
import com.toong.repository.*;
import com.toong.service.AdminTourService;
import com.toong.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTourServiceImpl implements AdminTourService {

        private final TourRepository tourRepository;
        private final PassOrderRepository passOrderRepository;
        private final MinioService minioService;
        private final TourCostDetailRepository tourCostDetailRepository;
        private final TourLuggageRepository tourLuggageRepository;
        private final TourFaqRepository tourFaqRepository;

        @Override
        public PaginationResponse<TourResponseDto> getAllTours(String name, int page, int limit) {
                Pageable pageable = PageRequest.of(page - 1, limit);
                Page<Tour> tourPage = tourRepository.findTourByName(name, pageable);
                var data = tourPage.getContent().stream()
                                .map(this::toDto)
                                .collect(Collectors.toList());
                return PaginationResponse.<TourResponseDto>builder()
                                .data(data)
                                .pagination(PaginationResponse.PaginationMeta.builder()
                                                .page(page).limit(limit).total(tourPage.getTotalElements()).build())
                                .build();
        }

        @Override
        public TourDetailResponseDto getTourDetail(Long tourId) {
                Tour tour = tourRepository.findById(tourId)
                                .orElseThrow(() -> new RuntimeException("Tour not found"));
                return toDetailDto(tour);
        }

        @Override
        @Transactional
        public TourResponseDto createTour(TourRequestDto request) {
                Tour tour = new Tour();
                mapRequestToTour(request, tour);
                Tour saved = tourRepository.save(tour);

                // Bulk-insert costDetails nếu list hợp lệ (khác null và không rỗng)
                if (request.getCostDetails() != null && !request.getCostDetails().isEmpty()) {
                        List<TourCostDetail> costDetails = request.getCostDetails().stream()
                                        .map(item -> TourCostDetail.builder()
                                                        .tour(saved)
                                                        .isIncluded(item.getIsIncluded())
                                                        .content(item.getContent())
                                                        .sortOrder(item.getSortOrder() != null ? item.getSortOrder()
                                                                        : 0)
                                                        .build())
                                        .collect(Collectors.toList());
                        tourCostDetailRepository.saveAll(costDetails);
                }

                // Bulk-insert luggages nếu list hợp lệ
                if (request.getLuggages() != null && !request.getLuggages().isEmpty()) {
                        List<TourLuggage> luggages = request.getLuggages().stream()
                                        .map(item -> TourLuggage.builder()
                                                        .tour(saved)
                                                        .name(item.getName())
                                                        .detail(item.getDetail())
                                                        .sortOrder(item.getSortOrder() != null ? item.getSortOrder()
                                                                        : 0)
                                                        .build())
                                        .collect(Collectors.toList());
                        tourLuggageRepository.saveAll(luggages);
                }

                // Bulk-insert faqs nếu list hợp lệ
                if (request.getFaqs() != null && !request.getFaqs().isEmpty()) {
                        List<TourFaq> faqs = request.getFaqs().stream()
                                        .map(item -> TourFaq.builder()
                                                        .tour(saved)
                                                        .question(item.getQuestion())
                                                        .answer(item.getAnswer())
                                                        .sortOrder(item.getSortOrder() != null ? item.getSortOrder()
                                                                        : 0)
                                                        .build())
                                        .collect(Collectors.toList());
                        tourFaqRepository.saveAll(faqs);
                }

                return toDto(saved);
        }

        @Override
        @Transactional
        public TourResponseDto updateTour(Long id, TourRequestDto request) {
                Tour tour = tourRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Tour not found"));
                mapRequestToTour(request, tour);
                Tour saved = tourRepository.save(tour);

                tourCostDetailRepository.deleteByTour_Id(id);
                if (request.getCostDetails() != null && !request.getCostDetails().isEmpty()) {
                        List<TourCostDetail> costDetails = request.getCostDetails().stream()
                                        .map(item -> TourCostDetail.builder()
                                                        .tour(saved)
                                                        .isIncluded(item.getIsIncluded())
                                                        .content(item.getContent())
                                                        .sortOrder(item.getSortOrder() != null ? item.getSortOrder()
                                                                        : 0)
                                                        .build())
                                        .collect(Collectors.toList());
                        tourCostDetailRepository.saveAll(costDetails);
                }

                tourLuggageRepository.deleteByTour_Id(id);
                if (request.getLuggages() != null && !request.getLuggages().isEmpty()) {
                        List<TourLuggage> luggages = request.getLuggages().stream()
                                        .map(item -> TourLuggage.builder()
                                                        .tour(saved)
                                                        .name(item.getName())
                                                        .detail(item.getDetail())
                                                        .sortOrder(item.getSortOrder() != null ? item.getSortOrder()
                                                                        : 0)
                                                        .build())
                                        .collect(Collectors.toList());
                        tourLuggageRepository.saveAll(luggages);
                }

                tourFaqRepository.deleteByTour_Id(id);
                if (request.getFaqs() != null && !request.getFaqs().isEmpty()) {
                        List<TourFaq> faqs = request.getFaqs().stream()
                                        .map(item -> TourFaq.builder()
                                                        .tour(saved)
                                                        .question(item.getQuestion())
                                                        .answer(item.getAnswer())
                                                        .sortOrder(item.getSortOrder() != null ? item.getSortOrder()
                                                                        : 0)
                                                        .build())
                                        .collect(Collectors.toList());
                        tourFaqRepository.saveAll(faqs);
                }

                return toDto(saved);
        }

        @Override
        public void deleteTour(Long id) {
                if (!tourRepository.existsById(id))
                        throw new RuntimeException("Tour not found");
                tourRepository.deleteById(id);
        }

        @Override
        public PaginationResponse<PassOrderResponseDto> getAllPassOrders(String status, int page, int limit) {
                Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
                Page<PassOrder> orderPage = passOrderRepository.findAll(pageable);
                var data = orderPage.getContent().stream()
                                .filter(o -> status == null || status.equals(o.getStatus()))
                                .map(this::toPassOrderDto)
                                .collect(Collectors.toList());
                return PaginationResponse.<PassOrderResponseDto>builder()
                                .data(data)
                                .pagination(PaginationResponse.PaginationMeta.builder()
                                                .page(page).limit(limit).total(orderPage.getTotalElements()).build())
                                .build();
        }

        @Override
        public PassOrderResponseDto updatePassOrderStatus(Long id, UpdateStatusDto dto) {
                PassOrder order = passOrderRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pass order not found"));
                order.setStatus(dto.getStatus());
                return toPassOrderDto(passOrderRepository.save(order));
        }

        private void mapRequestToTour(TourRequestDto req, Tour tour) {
                if (req.getName() != null)
                        tour.setName(req.getName());
                if (req.getSlug() != null)
                        tour.setSlug(req.getSlug());
                if (req.getHeroImage() != null)
                        tour.setHeroImage(req.getHeroImage());
                if (req.getCardImage() != null)
                        tour.setCardImage(req.getCardImage());
                if (req.getBadge() != null)
                        tour.setBadge(req.getBadge());
                if (req.getRegion() != null)
                        tour.setRegion(req.getRegion());
                if (req.getDurationDays() != null)
                        tour.setDurationDays(req.getDurationDays());
                if (req.getDurationNights() != null)
                        tour.setDurationNights(req.getDurationNights());
                if (req.getDifficulty() != null)
                        tour.setDifficulty(req.getDifficulty());
                if (req.getDistanceKm() != null)
                        tour.setDistanceKm(req.getDistanceKm());
                if (req.getMinAge() != null)
                        tour.setMinAge(req.getMinAge());
                if (req.getMaxAge() != null)
                        tour.setMaxAge(req.getMaxAge());
                if (req.getSummary() != null)
                        tour.setSummary(req.getSummary());
                if (req.getDescription() != null)
                        tour.setDescription(req.getDescription());
                if (req.getBasePrice() != null)
                        tour.setBasePrice(req.getBasePrice().doubleValue());
        }

        private TourResponseDto toDto(Tour t) {
                return TourResponseDto.builder()
                                .id(t.getId()).name(t.getName()).slug(t.getSlug())
                                .cardImage(minioService.getPresignedUrl(t.getCardImage())).badge(t.getBadge())
                                .region(t.getRegion())
                                .durationDays(t.getDurationDays()).durationNights(t.getDurationNights())
                                .difficulty(t.getDifficulty()).summary(t.getSummary())
                                .basePrice(t.getBasePrice()).build();
        }

        private PassOrderResponseDto toPassOrderDto(PassOrder o) {
                String passTitle = o.getPass() != null ? o.getPass().getTitle() : null;
                Long passId = o.getPass() != null ? o.getPass().getId() : null;
                return PassOrderResponseDto.builder()
                                .id(o.getId())
                                .orderCode(o.getOrderCode()).passId(passId).passTitle(passTitle)
                                .firstName(o.getFirstName()).lastName(o.getLastName())
                                .phone(o.getPhone()).email(o.getEmail())
                                .paymentMethod(o.getPaymentMethod() != null ? o.getPaymentMethod().name() : null)
                                .status(o.getStatus())
                                .build();
        }

        private TourDetailResponseDto toDetailDto(Tour t) {
                return TourDetailResponseDto.builder()
                                .id(t.getId()).name(t.getName()).slug(t.getSlug())
                                .cardImage(minioService.getPresignedUrl(t.getCardImage())).badge(t.getBadge())
                                .region(t.getRegion())
                                .durationDays(t.getDurationDays()).durationNights(t.getDurationNights())
                                .difficulty(t.getDifficulty()).summary(t.getSummary())
                                .basePrice(t.getBasePrice())
                                .costDetails(t.getCostDetails().stream().map(this::toCostDetailDto)
                                                .collect(Collectors.toList()))
                                .luggages(t.getLuggages().stream().map(this::toLuggageDto).collect(Collectors.toList()))
                                .faqs(t.getFaqs().stream().map(this::toFaqDto).collect(Collectors.toList()))
                                .build();
        }

        private TourCostDetailResponseDto toCostDetailDto(TourCostDetail c) {
                return TourCostDetailResponseDto.builder()
                                .id(c.getId()).isIncluded(c.getIsIncluded())
                                .content(c.getContent()).sortOrder(c.getSortOrder())
                                .build();
        }

        private TourLuggageResponseDto toLuggageDto(TourLuggage l) {
                return TourLuggageResponseDto.builder()
                                .id(l.getId()).name(l.getName()).detail(l.getDetail())
                                .sortOrder(l.getSortOrder())
                                .build();
        }

        private TourFaqResponseDto toFaqDto(TourFaq f) {
                return TourFaqResponseDto.builder()
                                .id(f.getId()).question(f.getQuestion()).answer(f.getAnswer())
                                .sortOrder(f.getSortOrder())
                                .build();
        }
}
