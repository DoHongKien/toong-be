package com.toong.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Kích hoạt @Async để NotificationEventListener xử lý bất đồng bộ,
 * tránh block luồng HTTP của BookingService, ContactService, PassOrderService.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
