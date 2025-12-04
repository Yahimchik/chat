package org.chat.chat_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationChatDto {

    private String type; // NEW_MESSAGE, MENTION, etc.
    private String title;
    private String message;
    private UUID relatedEntityId;
    private String relatedEntityType;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
}