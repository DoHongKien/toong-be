package com.toong.service.impl;

import com.toong.modal.dto.AdventurePassRequestDto;
import com.toong.modal.dto.AdventurePassResponseDto;
import com.toong.modal.dto.PassOrderRequestDto;
import com.toong.modal.entity.AdventurePass;
import com.toong.modal.entity.PassOrder;
import com.toong.repository.AdventurePassRepository;
import com.toong.repository.PassFeatureRepository;
import com.toong.repository.PassOrderRepository;
import com.toong.service.AdventurePassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdventurePassServiceImpl implements AdventurePassService {

    private final AdventurePassRepository adventurePassRepository;
    private final PassOrderRepository passOrderRepository;
    private final PassFeatureRepository passFeatureRepository;

    @Override
    public List<AdventurePassResponseDto> getAllPass() {
        return adventurePassRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public String createPassOrder(PassOrderRequestDto request) {
        AdventurePass pass = adventurePassRepository.findById(request.getPassId())
                .orElseThrow(() -> new RuntimeException("Pass not found"));

        String orderCode = "PASS" + System.currentTimeMillis();

        PassOrder order = new PassOrder();
        order.setOrderCode(orderCode);
        order.setPass(pass);
        order.setFirstName(request.getFirstName());
        order.setLastName(request.getLastName());
        order.setPhone(request.getPhone());
        order.setEmail(request.getEmail());
        order.setPaymentMethod(request.getPaymentMethod());

        passOrderRepository.save(order);
        return orderCode;
    }

    @Override
    public AdventurePassResponseDto createPass(AdventurePassRequestDto request) {
        AdventurePass pass = new AdventurePass();
        pass.setTitle(request.getTitle());
        pass.setSubtitle(request.getSubtitle());
        pass.setPrice(request.getPrice());
        pass.setValidityDate(request.getValidityDate());
        pass.setIsSignature(request.getIsSignature() != null ? request.getIsSignature() : false);
        pass.setColorTheme(request.getColorTheme());
        return toDto(adventurePassRepository.save(pass));
    }

    @Override
    public AdventurePassResponseDto updatePass(Long id, AdventurePassRequestDto request) {
        AdventurePass pass = adventurePassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pass not found: " + id));

        if (request.getTitle() != null) pass.setTitle(request.getTitle());
        if (request.getSubtitle() != null) pass.setSubtitle(request.getSubtitle());
        if (request.getPrice() != null) pass.setPrice(request.getPrice());
        if (request.getValidityDate() != null) pass.setValidityDate(request.getValidityDate());
        if (request.getIsSignature() != null) pass.setIsSignature(request.getIsSignature());
        if (request.getColorTheme() != null) pass.setColorTheme(request.getColorTheme());

        return toDto(adventurePassRepository.save(pass));
    }

    @Override
    public void deletePass(Long id) {
        if (!adventurePassRepository.existsById(id)) {
            throw new RuntimeException("Pass not found: " + id);
        }
        adventurePassRepository.deleteById(id);
    }

    private AdventurePassResponseDto toDto(AdventurePass pass) {
        List<AdventurePassResponseDto.PassFeatureDto> features = passFeatureRepository
                .findByPassIdOrderByIdAsc(pass.getId())
                .stream()
                .map(f -> AdventurePassResponseDto.PassFeatureDto.builder()
                        .content(f.getContent())
                        .isBold(f.getIsBold())
                        .build())
                .collect(Collectors.toList());

        return AdventurePassResponseDto.builder()
                .id(pass.getId())
                .title(pass.getTitle())
                .subtitle(pass.getSubtitle())
                .price(pass.getPrice())
                .validityDate(pass.getValidityDate())
                .isSignature(pass.getIsSignature())
                .colorTheme(pass.getColorTheme())
                .features(features)
                .build();
    }
}