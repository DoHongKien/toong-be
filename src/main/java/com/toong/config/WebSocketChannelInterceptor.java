package com.toong.config;

import com.toong.security.CustomUserDetailsService;
import com.toong.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Interceptor xác thực JWT trên STOMP CONNECT frame.
 * <p>
 * Client gửi JWT trong STOMP header:
 *   Authorization: Bearer <token>
 * <p>
 * Sau khi xác thực thành công, Spring sẽ bind Principal vào WebSocket session,
 * cho phép dùng convertAndSendToUser(username, ...) để route đúng user.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String authHeader = accessor.getFirstNativeHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            log.warn("[WS] CONNECT rejected: missing or invalid Authorization header");
            throw new IllegalArgumentException("WebSocket CONNECT: JWT token is required");
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtTokenProvider.validateToken(token, userDetails)) {
                throw new IllegalArgumentException("WebSocket CONNECT: JWT token is invalid or expired");
            }

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            accessor.setUser(auth);

            log.debug("[WS] CONNECT authenticated for user '{}'", username);
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            log.warn("[WS] CONNECT rejected for token: {}", ex.getMessage());
            throw new IllegalArgumentException("WebSocket CONNECT: authentication failed");
        }

        return message;
    }
}
