package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.EmployeeRequestDto;
import com.toong.modal.dto.EmployeeResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;
import com.toong.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/employees")
@RequiredArgsConstructor
public class AdminEmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<EmployeeResponseDto>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long roleId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.getAllEmployees(status, roleId, page, limit)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> create(@RequestBody EmployeeRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(employeeService.createEmployee(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> update(
            @PathVariable Long id, @RequestBody EmployeeRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.updateEmployee(id, request)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateStatus(
            @PathVariable Long id, @RequestBody UpdateStatusDto dto) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.updateStatus(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Nhân viên đã được xóa."));
    }
}
