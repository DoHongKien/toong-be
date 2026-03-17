package com.toong.service.impl;

import com.toong.modal.dto.DepartureRequestDto;
import com.toong.modal.dto.DepartureResponseDto;
import com.toong.modal.entity.Departure;
import com.toong.modal.entity.Tour;
import com.toong.repository.DepartureRepository;
import com.toong.repository.TourRepository;
import com.toong.service.AdminDepartureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDepartureServiceImpl implements AdminDepartureService {

    private final DepartureRepository departureRepository;
    private final TourRepository tourRepository;

    @Override
    public DepartureResponseDto createDeparture(DepartureRequestDto request) {
        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        Departure departure = new Departure();
        departure.setTour(tour);
        departure.setStartDate(request.getStartDate());
        departure.setEndDate(request.getEndDate());
        departure.setDepositDeadline(request.getDepositDeadline());
        departure.setPaymentDeadline(request.getPaymentDeadline());
        departure.setPrice(request.getPrice());
        departure.setTotalSlots(request.getTotalSlots());
        departure.setBookedSlots(0);
        departure.setStatus("active");

        Departure saved = departureRepository.save(departure);
        return toDto(saved);
    }

    @Override
    public DepartureResponseDto updateDeparture(Long id, DepartureRequestDto request) {
        Departure departure = departureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departure not found"));

        if (request.getStartDate() != null) departure.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) departure.setEndDate(request.getEndDate());
        if (request.getDepositDeadline() != null) departure.setDepositDeadline(request.getDepositDeadline());
        if (request.getPaymentDeadline() != null) departure.setPaymentDeadline(request.getPaymentDeadline());
        if (request.getPrice() != null) departure.setPrice(request.getPrice());
        if (request.getTotalSlots() != null) departure.setTotalSlots(request.getTotalSlots());

        return toDto(departureRepository.save(departure));
    }

    @Override
    public void deleteDeparture(Long id) {
        if (!departureRepository.existsById(id)) throw new RuntimeException("Departure not found");
        departureRepository.deleteById(id);
    }

    private DepartureResponseDto toDto(Departure d) {
        Integer available = d.getTotalSlots() - d.getBookedSlots();
        return DepartureResponseDto.builder()
                .id(d.getId())
                .startDate(d.getStartDate())
                .endDate(d.getEndDate())
                .depositDeadline(d.getDepositDeadline())
                .paymentDeadline(d.getPaymentDeadline())
                .price(d.getPrice())
                .availableSlots(available)
                .status(d.getStatus())
                .build();
    }
}
