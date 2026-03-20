package com.toong.service.impl;

import com.toong.modal.dto.*;
import com.toong.modal.entity.NotificationConfig;
import com.toong.modal.entity.NotificationRecipient;
import com.toong.modal.enums.TargetType;
import com.toong.repository.*;
import com.toong.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRecipientRepository recipientRepository;
    private final NotificationConfigRepository configRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    // ─── Notifications ───────────────────────────────────────────────────────

    @Override
    public NotificationListResponseDto getNotifications(Long employeeId, int limit, boolean unreadOnly) {
        List<NotificationRecipient> recipients = unreadOnly
                ? recipientRepository.findUnreadByEmployeeId(employeeId, limit)
                : recipientRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId, limit);

        List<NotificationResponseDto> data = recipients.stream()
                .map(r -> NotificationResponseDto.builder()
                        .id(r.getNotification().getId())
                        .type(r.getNotification().getType())
                        .title(r.getNotification().getTitle())
                        .description(r.getNotification().getDescription())
                        .refId(r.getNotification().getRefId())
                        .refPath(r.getNotification().getRefPath())
                        .isRead(r.getIsRead())
                        .createdAt(r.getNotification().getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        long unreadCount = recipientRepository.countByEmployeeIdAndIsReadFalse(employeeId);

        return NotificationListResponseDto.builder()
                .data(data)
                .unreadCount(unreadCount)
                .build();
    }

    @Override
    public void markAsRead(Long notificationId, Long employeeId) {
        NotificationRecipient recipient = recipientRepository
                .findByNotificationIdAndEmployeeId(notificationId, employeeId)
                .orElseThrow(() -> new RuntimeException("Thông báo không tồn tại."));

        if (!recipient.getIsRead()) {
            recipient.setIsRead(true);
            recipient.setReadAt(LocalDateTime.now());
            recipientRepository.save(recipient);
        }
    }

    @Override
    public void markAllAsRead(Long employeeId) {
        recipientRepository.markAllReadByEmployeeId(employeeId);
    }

    // ─── Notification Configs ─────────────────────────────────────────────────

    @Override
    public List<NotificationConfigResponseDto> getAllConfigs() {
        return configRepository.findAll().stream()
                .map(this::toConfigDto)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationConfigResponseDto createConfig(NotificationConfigRequestDto request) {
        validateTargetId(request.getTargetType(), request.getTargetId());

        if (configRepository.existsByNotifTypeAndTargetTypeAndTargetId(
                request.getNotifType(), request.getTargetType(), request.getTargetId())) {
            throw new RuntimeException("Cấu hình này đã tồn tại.");
        }

        NotificationConfig config = NotificationConfig.builder()
                .notifType(request.getNotifType())
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .build();

        return toConfigDto(configRepository.save(config));
    }

    @Override
    public NotificationConfigResponseDto updateConfig(Long id, NotificationConfigRequestDto request) {
        NotificationConfig config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cấu hình không tồn tại."));

        validateTargetId(request.getTargetType(), request.getTargetId());

        config.setNotifType(request.getNotifType());
        config.setTargetType(request.getTargetType());
        config.setTargetId(request.getTargetId());

        return toConfigDto(configRepository.save(config));
    }

    @Override
    public NotificationConfigResponseDto updateConfigStatus(Long id, NotificationConfigStatusDto dto) {
        NotificationConfig config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cấu hình không tồn tại."));
        config.setIsActive(dto.getIsActive());
        return toConfigDto(configRepository.save(config));
    }

    @Override
    public void deleteConfig(Long id) {
        if (!configRepository.existsById(id)) {
            throw new RuntimeException("Cấu hình không tồn tại.");
        }
        configRepository.deleteById(id);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private void validateTargetId(TargetType targetType, Long targetId) {
        if (targetType == TargetType.all && targetId != null) {
            throw new RuntimeException("targetId phải là null khi targetType = 'all'.");
        }
        if (targetType == TargetType.role) {
            if (targetId == null || !roleRepository.existsById(targetId)) {
                throw new RuntimeException("role_id không hợp lệ.");
            }
        }
        if (targetType == TargetType.employee) {
            if (targetId == null || !employeeRepository.existsById(targetId)) {
                throw new RuntimeException("employee_id không hợp lệ.");
            }
        }
    }

    private NotificationConfigResponseDto toConfigDto(NotificationConfig c) {
        String label = resolveLabel(c.getTargetType(), c.getTargetId());
        return NotificationConfigResponseDto.builder()
                .id(c.getId())
                .notifType(c.getNotifType())
                .targetType(c.getTargetType())
                .targetId(c.getTargetId())
                .targetLabel(label)
                .isActive(c.getIsActive())
                .createdAt(c.getCreatedAt())
                .build();
    }

    private String resolveLabel(TargetType targetType, Long targetId) {
        if (targetType == TargetType.all) return "Tất cả";
        if (targetId == null) return null;
        if (targetType == TargetType.role) {
            return roleRepository.findById(targetId)
                    .map(r -> r.getName())
                    .orElse("Unknown role");
        }
        if (targetType == TargetType.employee) {
            return employeeRepository.findById(targetId)
                    .map(e -> e.getFullName())
                    .orElse("Unknown employee");
        }
        return null;
    }
}
