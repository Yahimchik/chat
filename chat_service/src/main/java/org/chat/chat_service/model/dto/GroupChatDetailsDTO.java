package org.chat.chat_service.model.dto;

import org.chat.chat_service.model.enums.ChatType;

import java.time.Instant;
import java.util.UUID;

public record GroupChatDetailsDTO(
        UUID id,
        UUID chatRoomId,
        String title,
        String description,
        String coverUrl,
        ChatType type,
        UUID createdByUserId,
        Instant createdAt
) {
}
