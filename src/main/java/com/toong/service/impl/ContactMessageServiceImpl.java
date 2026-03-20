package com.toong.service.impl;

import com.toong.event.NotificationEvent;
import com.toong.modal.dto.ContactMessageResponseDto;
import com.toong.modal.dto.ContactRequestDto;
import com.toong.modal.dto.PaginationResponse;
import com.toong.modal.dto.UpdateStatusDto;
import com.toong.modal.entity.ContactMessage;
import com.toong.modal.enums.NotifType;
import com.toong.repository.ContactMessageRepository;
import com.toong.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void createMessage(ContactRequestDto request) {
        ContactMessage msg = ContactMessage.builder()
                .fullName(request.getFullName()).phone(request.getPhone())
                .email(request.getEmail()).message(request.getMessage())
                .status("new").createdAt(LocalDateTime.now()).build();
        ContactMessage saved = contactMessageRepository.save(msg);

        // Push notification
        eventPublisher.publishEvent(new NotificationEvent(
                this, NotifType.contact,
                "Liên hệ mới từ " + request.getFullName(),
                request.getMessage(),
                saved.getId(),
                "/cms/contacts"
        ));
    }

    @Override
    public PaginationResponse<ContactMessageResponseDto> getMessages(String status, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ContactMessage> msgPage = (status != null)
                ? contactMessageRepository.findByStatus(status, pageable)
                : contactMessageRepository.findAll(pageable);

        return PaginationResponse.<ContactMessageResponseDto>builder()
                .data(msgPage.getContent().stream().map(this::toDto).collect(Collectors.toList()))
                .pagination(PaginationResponse.PaginationMeta.builder()
                        .page(page).limit(limit).total(msgPage.getTotalElements()).build())
                .build();
    }

    @Override
    public ContactMessageResponseDto updateStatus(Long id, UpdateStatusDto dto) {
        ContactMessage msg = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact message not found"));
        msg.setStatus(dto.getStatus());
        return toDto(contactMessageRepository.save(msg));
    }

    @Override
    public void deleteMessage(Long id) {
        if (!contactMessageRepository.existsById(id)) throw new RuntimeException("Contact message not found");
        contactMessageRepository.deleteById(id);
    }

    private ContactMessageResponseDto toDto(ContactMessage m) {
        return ContactMessageResponseDto.builder()
                .id(m.getId()).fullName(m.getFullName()).phone(m.getPhone())
                .email(m.getEmail()).message(m.getMessage())
                .status(m.getStatus()).createdAt(m.getCreatedAt()).build();
    }
}
