package com.toong.service.impl;

import com.toong.modal.dto.EmployeeResponseDto;
import com.toong.modal.dto.LoginRequestDto;
import com.toong.modal.dto.LoginResponseDto;
import com.toong.modal.dto.RoleResponseDto;
import com.toong.modal.entity.Employee;
import com.toong.repository.EmployeeRepository;
import com.toong.security.CustomUserDetails;
import com.toong.security.JwtTokenProvider;
import com.toong.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider jwtTokenProvider;
        private final EmployeeRepository employeeRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public LoginResponseDto login(LoginRequestDto request) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                Employee employee = userDetails.getEmployee();

                String token = jwtTokenProvider.generateToken(employee.getUsername());

                // Update last login
                employee.setLastLogin(LocalDateTime.now());
                employeeRepository.save(employee);

                RoleResponseDto roleDto = null;
                if (employee.getRole() != null) {
                        roleDto = RoleResponseDto.builder()
                                        .id(employee.getRole().getId())
                                        .name(employee.getRole().getName())
                                        .code(employee.getRole().getCode())
                                        .build();
                }

                EmployeeResponseDto employeeDto = EmployeeResponseDto.builder()
                                .id(employee.getId())
                                .username(employee.getUsername())
                                .fullName(employee.getFullName())
                                .email(employee.getEmail())
                                .status(employee.getStatus())
                                .lastLogin(employee.getLastLogin())
                                .role(roleDto)
                                .build();

                return LoginResponseDto.builder()
                                .token(token)
                                .employee(employeeDto)
                                .build();
        }

        @Override
        public String encodePassword(String rawPassword) {
                return passwordEncoder.encode(rawPassword);
        }
}
