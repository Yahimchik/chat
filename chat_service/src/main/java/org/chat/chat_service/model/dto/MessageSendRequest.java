package org.chat.chat_service.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageSendRequest {
    private UUID roomId;
    private String content;
}
