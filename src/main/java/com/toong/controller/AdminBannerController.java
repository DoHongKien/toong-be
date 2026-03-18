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
@RequestMapping("/api/v1/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerResponseDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAllBanners()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BannerResponseDto>> create(@RequestBody BannerRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(bannerService.createBanner(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BannerResponseDto>> update(
            @PathVariable Long id, @RequestBody BannerRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.updateBanner(id, request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<BannerResponseDto>> patch(
            @PathVariable Long id, @RequestBody BannerRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.updateBanner(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.ok(ApiResponse.success("Banner đã được xóa."));
    }
}
