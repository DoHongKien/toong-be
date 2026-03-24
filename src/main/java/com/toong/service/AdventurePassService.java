package com.toong.service;

import com.toong.modal.dto.AdventurePassRequestDto;
import com.toong.modal.dto.AdventurePassResponseDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.PassOrderRequestDto;

import java.util.List;

public interface AdventurePassService {

    List<AdventurePassResponseDto> getAllPass();

    String createPassOrder(PassOrderRequestDto request);

    // Admin CRUD
    PaginationResponse<AdventurePassResponseDto> getAllPassForAdmin(int page, int limit);
    AdventurePassResponseDto createPass(AdventurePassRequestDto request);
    AdventurePassResponseDto updatePass(Long id, AdventurePassRequestDto request);
    void deletePass(Long id);
}