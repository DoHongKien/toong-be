package com.toong.service.impl;

import com.toong.modal.dto.PassOrderResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.TourRequestDto;
import com.toong.modal.dto.TourResponseDto;
import com.toong.modal.dto.UpdateStatusDto;
import com.toong.modal.entity.PassOrder;
import com.toong.modal.entity.Tour;
import com.toong.repository.PassOrderRepository;
import com.toong.repository.TourRepository;
import com.toong.service.AdminTourService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTourServiceImpl implements AdminTourService {

    private final TourRepository tourRepository;
    private final PassOrderRepository passOrderRepository;

    @Override
    public TourResponseDto createTour(TourRequestDto request) {
        Tour tour = new Tour();
        mapRequestToTour(request, tour);
        return toDto(tourRepository.save(tour));
    }

    @Override
    public TourResponseDto updateTour(Long id, TourRequestDto request) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
        mapRequestToTour(request, tour);
        return toDto(tourRepository.save(tour));
    }

    @Override
    public void deleteTour(Long id) {
        if (!tourRepository.existsById(id)) throw new RuntimeException("Tour not found");
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
        if (req.getName() != null) tour.setName(req.getName());
        if (req.getSlug() != null) tour.setSlug(req.getSlug());
        if (req.getHeroImage() != null) tour.setHeroImage(req.getHeroImage());
        if (req.getCardImage() != null) tour.setCardImage(req.getCardImage());
        if (req.getBadge() != null) tour.setBadge(req.getBadge());
        if (req.getRegion() != null) tour.setRegion(req.getRegion());
        if (req.getDurationDays() != null) tour.setDurationDays(req.getDurationDays());
        if (req.getDurationNights() != null) tour.setDurationNights(req.getDurationNights());
        if (req.getDifficulty() != null) tour.setDifficulty(req.getDifficulty());
        if (req.getDistanceKm() != null) tour.setDistanceKm(req.getDistanceKm());
        if (req.getMinAge() != null) tour.setMinAge(req.getMinAge());
        if (req.getMaxAge() != null) tour.setMaxAge(req.getMaxAge());
        if (req.getSummary() != null) tour.setSummary(req.getSummary());
        if (req.getDescription() != null) tour.setDescription(req.getDescription());
        if (req.getBasePrice() != null) tour.setBasePrice(req.getBasePrice().doubleValue());
    }

    private TourResponseDto toDto(Tour t) {
        return TourResponseDto.builder()
                .id(t.getId()).name(t.getName()).slug(t.getSlug())
                .cardImage(t.getCardImage()).badge(t.getBadge()).region(t.getRegion())
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
}
