package com.toong.service;

import com.toong.modal.dto.NotificationConfigRequestDto;
import com.toong.modal.dto.NotificationConfigResponseDto;
import com.toong.modal.dto.NotificationConfigStatusDto;
import com.toong.modal.dto.NotificationListResponseDto;

import java.util.List;

public interface NotificationService {
    NotificationListResponseDto getNotifications(Long employeeId, int limit, boolean unreadOnly);
    void markAsRead(Long notificationId, Long employeeId);
    void markAllAsRead(Long employeeId);

    // Config
    List<NotificationConfigResponseDto> getAllConfigs();
    NotificationConfigResponseDto createConfig(NotificationConfigRequestDto request);
    NotificationConfigResponseDto updateConfig(Long id, NotificationConfigRequestDto request);
    NotificationConfigResponseDto updateConfigStatus(Long id, NotificationConfigStatusDto dto);
    void deleteConfig(Long id);
}
