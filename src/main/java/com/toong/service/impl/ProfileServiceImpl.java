package com.toong.service.impl;

import com.toong.modal.dto.ChangePasswordRequestDto;
import com.toong.modal.dto.EmployeeResponseDto;
import com.toong.modal.dto.ProfileUpdateRequestDto;
import com.toong.modal.dto.RoleResponseDto;
import com.toong.modal.entity.Employee;
import com.toong.repository.EmployeeRepository;
import com.toong.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResponseDto getProfile(String username) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return toDto(employee);
    }

    @Override
    public EmployeeResponseDto updateProfile(String username, ProfileUpdateRequestDto request) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (request.getEmail() != null && !request.getEmail().equals(employee.getEmail())) {
            boolean emailTaken = employeeRepository.existsByEmailAndIdNot(request.getEmail(), employee.getId());
            if (emailTaken) {
                throw new RuntimeException("Email đã được sử dụng bởi nhân viên khác.");
            }
            employee.setEmail(request.getEmail());
        }
        if (request.getFullName() != null) {
            employee.setFullName(request.getFullName());
        }

        return toDto(employeeRepository.save(employee));
    }

    @Override
    public void changePassword(String username, ChangePasswordRequestDto request) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), employee.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng.");
        }

        employee.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        employeeRepository.save(employee);
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
                .id(e.getId())
                .username(e.getUsername())
                .fullName(e.getFullName())
                .email(e.getEmail())
                .status(e.getStatus())
                .lastLogin(e.getLastLogin())
                .createdAt(e.getCreatedAt())
                .role(roleDto)
                .build();
    }
}
