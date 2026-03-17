package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.AssignPermissionsDto;
import com.toong.modal.dto.PermissionResponseDto;
import com.toong.modal.dto.RoleRequestDto;
import com.toong.modal.dto.RoleResponseDto;
import com.toong.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminRoleController {

    private final RoleService roleService;

    // ---- Roles ----
    @GetMapping("/api/v1/admin/roles")
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> getAllRoles() {
        return ResponseEntity.ok(ApiResponse.success(roleService.getAllRoles()));
    }

    @PostMapping("/api/v1/admin/roles")
    public ResponseEntity<ApiResponse<RoleResponseDto>> createRole(@RequestBody RoleRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(roleService.createRole(request)));
    }

    @PutMapping("/api/v1/admin/roles/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDto>> updateRole(
            @PathVariable Long id, @RequestBody RoleRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(roleService.updateRole(id, request)));
    }

    @DeleteMapping("/api/v1/admin/roles/{id}")
    public ResponseEntity<ApiResponse<String>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success("Vai trò đã được xóa."));
    }

    // ---- Permissions ----
    @GetMapping("/api/v1/admin/permissions")
    public ResponseEntity<ApiResponse<List<PermissionResponseDto>>> getAllPermissions() {
        return ResponseEntity.ok(ApiResponse.success(roleService.getAllPermissions()));
    }

    @GetMapping("/api/v1/admin/roles/{id}/permissions")
    public ResponseEntity<ApiResponse<List<PermissionResponseDto>>> getPermissionsByRole(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(roleService.getPermissionsByRole(id)));
    }

    @PutMapping("/api/v1/admin/roles/{id}/permissions")
    public ResponseEntity<ApiResponse<RoleResponseDto>> assignPermissions(
            @PathVariable Long id, @RequestBody AssignPermissionsDto dto) {
        return ResponseEntity.ok(ApiResponse.success(roleService.assignPermissions(id, dto)));
    }
}
