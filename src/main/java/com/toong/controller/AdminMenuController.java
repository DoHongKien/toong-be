package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.*;
import com.toong.modal.enums.MenuContext;
import com.toong.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/menus")
@RequiredArgsConstructor
public class AdminMenuController {

    private final MenuService menuService;

    /** 18.2 / 18.9 - Lấy toàn bộ menu theo context (mặc định CLIENT, truyền ?context=CMS để lấy sidebar CMS) */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuAdminResponseDto>>> getAll(
            @RequestParam(required = false, defaultValue = "CLIENT") MenuContext context) {
        return ResponseEntity.ok(ApiResponse.success(menuService.getAllMenusForAdmin(context)));
    }

    /** 18.3 - Lấy chi tiết một menu theo ID */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuAdminResponseDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(menuService.getMenuById(id)));
    }

    /** 18.4 - Tạo menu mới */
    @PostMapping
    public ResponseEntity<ApiResponse<MenuAdminResponseDto>> create(@RequestBody MenuRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(menuService.createMenu(request)));
    }

    /** 18.5 - Cập nhật menu */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MenuAdminResponseDto>> update(
            @PathVariable Long id, @RequestBody MenuRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(menuService.updateMenu(id, request)));
    }

    /** 18.6 - Xóa menu (cascade xóa cả menu con) */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa menu."));
    }

    /** 18.7 - Bật/Tắt hiển thị menu */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<MenuAdminResponseDto>> updateStatus(
            @PathVariable Long id, @RequestBody MenuStatusDto dto) {
        return ResponseEntity.ok(ApiResponse.success(menuService.updateMenuStatus(id, dto)));
    }

    /** 18.8 - Cập nhật thứ tự hiển thị menu */
    @PatchMapping("/{id}/order")
    public ResponseEntity<ApiResponse<MenuAdminResponseDto>> updateOrder(
            @PathVariable Long id, @RequestBody MenuOrderDto dto) {
        return ResponseEntity.ok(ApiResponse.success(menuService.updateMenuOrder(id, dto)));
    }
}
