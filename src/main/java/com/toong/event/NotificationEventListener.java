package com.toong.event;

import com.toong.modal.entity.Employee;
import com.toong.modal.entity.Notification;
import com.toong.modal.entity.NotificationConfig;
import com.toong.modal.entity.NotificationRecipient;
import com.toong.modal.enums.TargetType;
import com.toong.repository.EmployeeRepository;
import com.toong.repository.NotificationConfigRepository;
import com.toong.repository.NotificationRecipientRepository;
import com.toong.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
                            .build()
            );

            // 2. Tra notification_configs để tìm danh sách employee nhận thông báo
            List<NotificationConfig> configs = configRepository.findAll().stream()
                    .filter(c -> c.getIsActive() && c.getNotifType() == event.getType())
                    .toList();

            Set<Long> employeeIds = new HashSet<>();
            for (NotificationConfig config : configs) {
                switch (config.getTargetType()) {
                    case all -> employeeRepository.findAll().stream()
                            .filter(e -> "active".equals(e.getStatus()))
                            .map(Employee::getId)
                            .forEach(employeeIds::add);

                    case role -> {
                        if (config.getTargetId() != null) {
                            employeeRepository.findAll().stream()
                                    .filter(e -> e.getRole() != null
                                            && e.getRole().getId().equals(config.getTargetId())
                                            && "active".equals(e.getStatus()))
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
            List<NotificationRecipient> recipients = new ArrayList<>();
            for (Long empId : employeeIds) {
                employeeRepository.findById(empId).ifPresent(employee ->
                        recipients.add(NotificationRecipient.builder()
                                .notification(notification)
                                .employee(employee)
                                .isRead(false)
                                .build())
                );
            }
            recipientRepository.saveAll(recipients);

            log.info("[Notification] Đã push '{}' tới {} người nhận.", event.getTitle(), recipients.size());
        } catch (Exception ex) {
            log.error("[Notification] Lỗi khi push thông báo: {}", ex.getMessage(), ex);
        }
    }
}
