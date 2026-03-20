package com.toong.modal.dto;

import com.toong.modal.enums.NotifType;
import com.toong.modal.enums.TargetType;
import lombok.Data;

@Data
public class NotificationConfigRequestDto {
    private NotifType notifType;
    private TargetType targetType;
    private Long targetId;
}
