package com.toong.modal.dto;

import com.toong.modal.enums.NotifType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private NotifType type;
    private String title;
    private String description;
    private Long refId;
    private String refPath;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
