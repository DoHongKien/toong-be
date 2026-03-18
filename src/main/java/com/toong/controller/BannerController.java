package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.BannerResponseDto;
import com.toong.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerResponseDto>>> getActiveBanners() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getActiveBanners()));
    }
}
