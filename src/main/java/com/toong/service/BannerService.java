package com.toong.service;

import com.toong.modal.dto.BannerRequestDto;
import com.toong.modal.dto.BannerResponseDto;

import java.util.List;

public interface BannerService {
    List<BannerResponseDto> getActiveBanners();
    List<BannerResponseDto> getAllBanners();
    BannerResponseDto createBanner(BannerRequestDto request);
    BannerResponseDto updateBanner(Long id, BannerRequestDto request);
    void deleteBanner(Long id);
}
