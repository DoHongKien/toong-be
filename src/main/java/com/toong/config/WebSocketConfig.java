package com.toong.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketChannelInterceptor channelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint để client kết nối, hỗ trợ SockJS fallback
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefix cho messages gửi từ client lên server (nếu cần two-way)
        registry.setApplicationDestinationPrefixes("/app");

        // Prefix cho user-specific destinations: /user/{username}/queue/...
        registry.setUserDestinationPrefix("/user");

        // In-memory broker (đủ dùng; nếu scale multi-instance sau này đổi sang RabbitMQ/Redis)
        registry.enableSimpleBroker("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Đăng ký JWT interceptor — chạy trước khi xử lý mọi STOMP frame
        registration.interceptors(channelInterceptor);
    }
}
