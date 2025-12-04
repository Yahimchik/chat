package org.chat.chat_service.model.dto;

import org.chat.chat_service.model.enums.ChatType;

public record GroupChatCreateRequest(
        String title,
        String description,
        String coverUrl,
        ChatType type
) {
}
