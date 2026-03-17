package com.toong.service.impl;

import com.toong.modal.dto.BannerRequestDto;
import com.toong.modal.dto.BannerResponseDto;
import com.toong.modal.entity.Banner;
import com.toong.repository.BannerRepository;
import com.toong.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    public List<BannerResponseDto> getActiveBanners() {
        return bannerRepository.findByIsActiveTrueOrderBySortOrderAsc()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BannerResponseDto> getAllBanners() {
        return bannerRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public BannerResponseDto createBanner(BannerRequestDto request) {
        Banner banner = Banner.builder()
                .title(request.getTitle()).imageUrl(request.getImageUrl())
                .linkUrl(request.getLinkUrl())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        return toDto(bannerRepository.save(banner));
    }

    @Override
    public BannerResponseDto updateBanner(Long id, BannerRequestDto request) {
        Banner banner = bannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Banner not found"));
        if (request.getTitle() != null) banner.setTitle(request.getTitle());
        if (request.getImageUrl() != null) banner.setImageUrl(request.getImageUrl());
        if (request.getLinkUrl() != null) banner.setLinkUrl(request.getLinkUrl());
        if (request.getSortOrder() != null) banner.setSortOrder(request.getSortOrder());
        if (request.getIsActive() != null) banner.setIsActive(request.getIsActive());
        return toDto(bannerRepository.save(banner));
    }

    @Override
    public void deleteBanner(Long id) {
        if (!bannerRepository.existsById(id)) throw new RuntimeException("Banner not found");
        bannerRepository.deleteById(id);
    }

    private BannerResponseDto toDto(Banner b) {
        return BannerResponseDto.builder()
                .id(b.getId()).title(b.getTitle()).imageUrl(b.getImageUrl())
                .linkUrl(b.getLinkUrl()).sortOrder(b.getSortOrder()).isActive(b.getIsActive()).build();
    }
}
