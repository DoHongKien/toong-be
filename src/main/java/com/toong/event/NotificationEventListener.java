package com.toong.event;

import com.toong.modal.dto.NotificationResponseDto;
import com.toong.modal.entity.Employee;
import com.toong.modal.entity.Notification;
import com.toong.modal.entity.NotificationConfig;
import com.toong.modal.entity.NotificationRecipient;
import com.toong.repository.EmployeeRepository;
import com.toong.repository.NotificationConfigRepository;
import com.toong.repository.NotificationRecipientRepository;
import com.toong.repository.NotificationRepository;
import com.toong.websocket.NotificationWebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository recipientRepository;
    private final NotificationConfigRepository configRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationWebSocketService webSocketService;

    @Async
    @EventListener
    @Transactional
    public void handleNotificationEvent(NotificationEvent event) {
        try {
            // 1. Lưu thông báo vào bảng notifications
            Notification notification = notificationRepository.save(
                    Notification.builder()
                            .type(event.getType())
                            .title(event.getTitle())
                            .description(event.getDescription())
                            .refId(event.getRefId())
                            .refPath(event.getRefPath())
                            .build());

            // 2. Tra notification_configs để tìm danh sách employee nhận thông báo
            List<NotificationConfig> configs = configRepository.findByIsActiveTrueAndNotifType(event.getType());

            Set<Long> employeeIds = new HashSet<>();
            for (NotificationConfig config : configs) {
                switch (config.getTargetType()) {
                    case all -> employeeRepository.findByStatus("active").stream()
                            .map(Employee::getId)
                            .forEach(employeeIds::add);

                    case role -> {
                        if (config.getTargetId() != null) {
                            employeeRepository.findByRoleIdAndStatus(config.getTargetId(), "active").stream()
                                    .map(Employee::getId)
                                    .forEach(employeeIds::add);
                        }
                    }

                    case employee -> {
                        if (config.getTargetId() != null) {
                            employeeIds.add(config.getTargetId());
                        }
                    }
                }
            }

            // 3. Tạo NotificationRecipient cho từng employee
            List<NotificationRecipient> recipients = employeeRepository.findAllById(employeeIds).stream()
                    .map(employee -> NotificationRecipient.builder()
                            .notification(notification)
                            .employee(employee)
                            .isRead(false)
                            .build())
                    .toList();
            recipientRepository.saveAll(recipients);

            log.info("[Notification] Đã push '{}' tới {} người nhận.", event.getTitle(), recipients.size());

            // 4. Push real-time qua WebSocket (best-effort — DB đã được lưu trước đó)
            //    Nếu user offline, message bị drop silently, không ảnh hưởng dữ liệu.
            for (NotificationRecipient recipient : recipients) {
                String username = recipient.getEmployee().getUsername();
                NotificationResponseDto dto = NotificationResponseDto.builder()
                        .id(notification.getId())
                        .type(notification.getType())
                        .title(notification.getTitle())
                        .description(notification.getDescription())
                        .refId(notification.getRefId())
                        .refPath(notification.getRefPath())
                        .isRead(false)
                        .createdAt(notification.getCreatedAt())
                        .build();
                webSocketService.sendToUser(username, dto);
            }

        } catch (Exception ex) {
            log.error("[Notification] Lỗi khi push thông báo: {}", ex.getMessage(), ex);
        }
    }
}
