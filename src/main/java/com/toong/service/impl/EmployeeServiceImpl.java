package com.toong.service.impl;

import com.toong.modal.dto.EmployeeDropdownDto;
import com.toong.modal.dto.EmployeeRequestDto;
import com.toong.modal.dto.EmployeeResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.RoleResponseDto;
import com.toong.modal.dto.UpdateStatusDto;
import com.toong.modal.entity.Employee;
import com.toong.modal.entity.Role;
import com.toong.repository.EmployeeRepository;
import com.toong.repository.RoleRepository;
import com.toong.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PaginationResponse<EmployeeResponseDto> getAllEmployees(String status, Long roleId, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeResponseDto> data = employeePage.getContent().stream()
                .filter(e -> status == null || e.getStatus().equals(status))
                .filter(e -> roleId == null || (e.getRole() != null && e.getRole().getId().equals(roleId)))
                .map(this::toDto)
                .collect(Collectors.toList());

        return PaginationResponse.<EmployeeResponseDto>builder()
                .data(data)
                .pagination(PaginationResponse.PaginationMeta.builder()
                        .page(page).limit(limit).total(employeePage.getTotalElements()).build())
                .build();
    }

    @Override
    public List<EmployeeDropdownDto> getAllEmployeesForDropdown() {
        return employeeRepository.findAll().stream()
                .filter(e -> "active".equals(e.getStatus()))
                .map(e -> {
                    RoleResponseDto roleDto = null;
                    if (e.getRole() != null) {
                        roleDto = RoleResponseDto.builder()
                                .id(e.getRole().getId())
                                .name(e.getRole().getName())
                                .code(e.getRole().getCode())
                                .build();
                    }
                    return EmployeeDropdownDto.builder()
                            .id(e.getId())
                            .fullName(e.getFullName())
                            .role(roleDto)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDto createEmployee(EmployeeRequestDto request) {
        if (employeeRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Employee employee = Employee.builder()
                .role(role)
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .status("active")
                .createdAt(LocalDateTime.now())
                .build();

        return toDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            employee.setRole(role);
        }
        if (request.getFullName() != null) employee.setFullName(request.getFullName());
        if (request.getEmail() != null) employee.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            employee.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        return toDto(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) throw new RuntimeException("Employee not found");
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeResponseDto updateStatus(Long id, UpdateStatusDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setStatus(dto.getStatus());
        return toDto(employeeRepository.save(employee));
    }

    private EmployeeResponseDto toDto(Employee e) {
        RoleResponseDto roleDto = null;
        if (e.getRole() != null) {
            roleDto = RoleResponseDto.builder()
                    .id(e.getRole().getId())
                    .name(e.getRole().getName())
                    .code(e.getRole().getCode())
                    .build();
        }
        return EmployeeResponseDto.builder()
                .id(e.getId()).username(e.getUsername()).fullName(e.getFullName())
                .email(e.getEmail()).status(e.getStatus()).lastLogin(e.getLastLogin())
                .createdAt(e.getCreatedAt()).role(roleDto).build();
    }
}
