package com.toong.repository;

import com.toong.modal.entity.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Long> {

    @Query("""
        SELECT r FROM NotificationRecipient r
        JOIN FETCH r.notification n
        WHERE r.employee.id = :employeeId
        ORDER BY n.createdAt DESC
        LIMIT :limit
    """)
    List<NotificationRecipient> findByEmployeeIdOrderByCreatedAtDesc(
            @Param("employeeId") Long employeeId,
            @Param("limit") int limit);

    @Query("""
        SELECT r FROM NotificationRecipient r
        JOIN FETCH r.notification n
        WHERE r.employee.id = :employeeId AND r.isRead = false
        ORDER BY n.createdAt DESC
        LIMIT :limit
    """)
    List<NotificationRecipient> findUnreadByEmployeeId(
            @Param("employeeId") Long employeeId,
            @Param("limit") int limit);

    long countByEmployeeIdAndIsReadFalse(Long employeeId);

    Optional<NotificationRecipient> findByNotificationIdAndEmployeeId(Long notificationId, Long employeeId);

    List<NotificationRecipient> findByEmployeeIdAndIsReadFalse(Long employeeId);

    @Modifying
    @Transactional
    @Query("UPDATE NotificationRecipient r SET r.isRead = true, r.readAt = CURRENT_TIMESTAMP WHERE r.employee.id = :employeeId AND r.isRead = false")
    void markAllReadByEmployeeId(@Param("employeeId") Long employeeId);
}
