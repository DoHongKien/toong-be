package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.MenuResponseDto;
import com.toong.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuResponseDto>>> getMenu() {
        return ResponseEntity.ok(ApiResponse.success(menuService.getMainMenu()));
    }
}
