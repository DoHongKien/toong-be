package com.toong.service;

import com.toong.modal.dto.ContactMessageResponseDto;
import com.toong.modal.dto.ContactRequestDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;

public interface ContactMessageService {
    void createMessage(ContactRequestDto request);
    PaginationResponse<ContactMessageResponseDto> getMessages(String status, int page, int limit);
    ContactMessageResponseDto updateStatus(Long id, UpdateStatusDto dto);
    void deleteMessage(Long id);
}
