package com.simple.taxi.auth.service.impl;

import com.simple.taxi.auth.kafka.EventProducer;
import com.simple.taxi.auth.model.dto.notification.NotificationChannel;
import com.simple.taxi.auth.model.dto.notification.NotificationEvent;
import com.simple.taxi.auth.model.dto.notification.NotificationType;
import com.simple.taxi.auth.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static com.simple.taxi.auth.model.dto.notification.NotificationChannel.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final EventProducer eventProducer;

    @Override
    public void sendEmail(UUID userId, String email, NotificationType type, Map<String, Object> params) {
        NotificationEvent event = NotificationEvent.builder()
                .userId(userId.toString())
                .recipient(email)
                .channel(EMAIL)
                .type(type)
                .params(params)
                .build();

        eventProducer.sendEvent("notification-topic", userId.toString(), event);
    }
}
