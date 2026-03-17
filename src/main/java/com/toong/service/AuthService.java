package com.toong.service;

import com.toong.modal.dto.LoginRequestDto;
import com.toong.modal.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto request);
}
