package com.toong.modal.dto;

import com.toong.modal.enums.NotifType;
import com.toong.modal.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationConfigResponseDto {
    private Long id;
    private NotifType notifType;
    private TargetType targetType;
    private Long targetId;
    private String targetLabel;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
