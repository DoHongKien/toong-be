package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.PassOrderRequestDto;
import com.toong.service.AdventurePassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/adventure-passes")
@RequiredArgsConstructor
public class AdventurePassController {

    private final AdventurePassService adventurePassService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllPass() {
        return ResponseEntity.ok(ApiResponse.success(adventurePassService.getAllPass()));
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponse<?>> orderPass(@RequestBody PassOrderRequestDto request) {
        String orderCode = adventurePassService.createPassOrder(request);
        return ResponseEntity.ok(ApiResponse.success("Order created successfully", orderCode));
    }
}