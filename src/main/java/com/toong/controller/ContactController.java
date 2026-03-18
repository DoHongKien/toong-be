package com.toong.controller;

import com.toong.modal.ApiResponse;
import com.toong.modal.dto.ContactRequestDto;
import com.toong.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageService contactMessageService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> submit(@RequestBody ContactRequestDto request) {
        contactMessageService.createMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tin nhắn đã được gửi."));
    }
}
