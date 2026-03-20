package com.toong.service;

import com.toong.modal.dto.EmployeeDropdownDto;
import com.toong.modal.dto.EmployeeRequestDto;
import com.toong.modal.dto.EmployeeResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;

import java.util.List;

public interface EmployeeService {
    PaginationResponse<EmployeeResponseDto> getAllEmployees(String status, Long roleId, int page, int limit);
    List<EmployeeDropdownDto> getAllEmployeesForDropdown();
    EmployeeResponseDto createEmployee(EmployeeRequestDto request);
    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto request);
    void deleteEmployee(Long id);
    EmployeeResponseDto updateStatus(Long id, UpdateStatusDto dto);
}
