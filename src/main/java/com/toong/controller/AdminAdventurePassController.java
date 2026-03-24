package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.AdventurePassRequestDto;
import com.toong.modal.dto.AdventurePassResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.service.AdventurePassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/adventure-passes")
@RequiredArgsConstructor
public class AdminAdventurePassController {

    private final AdventurePassService adventurePassService;

    /** 3.6 - Lấy danh sách Adventure Pass có phân trang, sort theo giá tăng dần */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<AdventurePassResponseDto>>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(adventurePassService.getAllPassForAdmin(page, limit)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdventurePassResponseDto>> create(
            @RequestBody AdventurePassRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(adventurePassService.createPass(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdventurePassResponseDto>> update(
            @PathVariable Long id, @RequestBody AdventurePassRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(adventurePassService.updatePass(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        adventurePassService.deletePass(id);
        return ResponseEntity.ok(ApiResponse.success("Adventure Pass đã được xóa."));
    }
}
