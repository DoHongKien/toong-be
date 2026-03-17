package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.BannerRequestDto;
import com.toong.modal.dto.BannerResponseDto;
import com.toong.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    /** PUBLIC: Lấy banner đang active */
    @GetMapping("/api/v1/banners")
    public ResponseEntity<ApiResponse<List<BannerResponseDto>>> getActiveBanners() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getActiveBanners()));
    }

    /** ADMIN: Lấy tất cả banner (kể cả inactive) */
    @GetMapping("/api/v1/admin/banners")
    public ResponseEntity<ApiResponse<List<BannerResponseDto>>> getAllBanners() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAllBanners()));
    }

    @PostMapping("/api/v1/admin/banners")
    public ResponseEntity<ApiResponse<BannerResponseDto>> create(@RequestBody BannerRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(bannerService.createBanner(request)));
    }

    @PutMapping("/api/v1/admin/banners/{id}")
    public ResponseEntity<ApiResponse<BannerResponseDto>> update(
            @PathVariable Long id, @RequestBody BannerRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.updateBanner(id, request)));
    }

    @PatchMapping("/api/v1/admin/banners/{id}")
    public ResponseEntity<ApiResponse<BannerResponseDto>> patch(
            @PathVariable Long id, @RequestBody BannerRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.updateBanner(id, request)));
    }

    @DeleteMapping("/api/v1/admin/banners/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok(ApiResponse.success("Banner đã được xóa."));
    }
}
