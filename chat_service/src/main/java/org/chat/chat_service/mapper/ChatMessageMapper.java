package org.chat.chat_service.mapper;

import org.chat.chat_service.model.dto.ChatMessageDTO;
import org.chat.chat_service.model.entities.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "senderName", ignore = true)
    @Mapping(target = "senderAvatarUrl", ignore = true)
    ChatMessageDTO toDto(ChatMessage entity);
}

