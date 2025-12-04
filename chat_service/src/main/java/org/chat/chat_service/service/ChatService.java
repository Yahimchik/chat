package org.chat.chat_service.service;

import org.chat.chat_service.model.dto.ChatMessageDTO;
import org.chat.chat_service.model.dto.MessageSendRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ChatService {

    ChatMessageDTO sendMessage(MessageSendRequest request, UUID senderId);

    Page<ChatMessageDTO> getMessagesForRoom(UUID roomId, UUID currentUserId, Pageable pageable);

    UUID findOrCreatePrivateRoom(UUID userFirstId, UUID userSecondId);
}