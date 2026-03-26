package com.toong.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Push một payload tới user cụ thể qua WebSocket.
     * <p>
     * Client subscribe vào: /user/queue/notifications
     * Spring tự resolve thành: /user/{username}/queue/notifications
     * <p>
     * Nếu user chưa connect hoặc đã disconnect, message sẽ bị drop silently —
     * không gây lỗi, không ảnh hưởng luồng DB vì thông báo đã được lưu trước đó.
     *
     * @param username JWT subject (employee username)
     * @param payload  Object sẽ được serialize thành JSON
     */
    public void sendToUser(String username, Object payload) {
        try {
            messagingTemplate.convertAndSendToUser(username, "/queue/notifications", payload);
            log.debug("[WS] Pushed notification to user '{}'", username);
        } catch (Exception ex) {
            log.warn("[WS] Failed to push notification to '{}': {}", username, ex.getMessage());
        }
    }
}
