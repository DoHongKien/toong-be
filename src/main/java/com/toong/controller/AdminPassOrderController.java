package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.PassOrderResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;
import com.toong.service.AdminTourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/pass-orders")
@RequiredArgsConstructor
public class AdminPassOrderController {

    private final AdminTourService adminTourService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<PassOrderResponseDto>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                adminTourService.getAllPassOrders(status, page, limit)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PassOrderResponseDto>> updateStatus(
            @PathVariable Long id, @RequestBody UpdateStatusDto dto) {
        return ResponseEntity.ok(ApiResponse.success(adminTourService.updatePassOrderStatus(id, dto)));
    }
}
