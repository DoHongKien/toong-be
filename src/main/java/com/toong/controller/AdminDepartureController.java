package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.DepartureRequestDto;
import com.toong.modal.dto.DepartureResponseDto;
import com.toong.service.AdminDepartureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/departures")
@RequiredArgsConstructor
public class AdminDepartureController {

    private final AdminDepartureService adminDepartureService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartureResponseDto>> create(@RequestBody DepartureRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(adminDepartureService.createDeparture(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartureResponseDto>> update(
            @PathVariable Long id, @RequestBody DepartureRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(adminDepartureService.updateDeparture(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        adminDepartureService.deleteDeparture(id);
        return ResponseEntity.ok(ApiResponse.success("Lịch khởi hành đã được xóa."));
    }
}
