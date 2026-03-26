package com.toong.repository;

import com.toong.modal.entity.NotificationConfig;
import com.toong.modal.enums.NotifType;
import com.toong.modal.enums.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationConfigRepository extends JpaRepository<NotificationConfig, Long> {

    List<NotificationConfig> findByIsActiveTrue();

    List<NotificationConfig> findByIsActiveTrueAndNotifType(NotifType notifType);

    boolean existsByNotifTypeAndTargetTypeAndTargetId(
            NotifType notifType, TargetType targetType, Long targetId);
}
