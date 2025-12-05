package com.simple.taxi.auth.model.dto.notification;

import lombok.Builder;

import java.util.Map;

@Builder
public record NotificationEvent(
        String userId,
        String recipient,
        NotificationChannel channel,
        NotificationType type,
        Map<String, Object> params
) {
}