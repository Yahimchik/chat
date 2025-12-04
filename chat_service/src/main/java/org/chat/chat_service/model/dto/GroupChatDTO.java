package org.chat.chat_service.model.dto;

import java.util.UUID;

public record GroupChatDTO(
        UUID id,
        String title,
        String coverUrl,
        int memberCount
) {
}
