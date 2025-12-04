package org.chat.chat_service.mapper;

import org.chat.chat_service.model.dto.GroupChatCreateRequest;
import org.chat.chat_service.model.dto.GroupChatDTO;
import org.chat.chat_service.model.dto.GroupChatDetailsDTO;
import org.chat.chat_service.model.entities.GroupChat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupChatMapper {

    GroupChat toEntity(GroupChatCreateRequest request);

    @Mapping(target = "chatRoomId", source = "chatRoom.id")
    GroupChatDetailsDTO toDetailsDto(GroupChat chat);

    @Mapping(
            target = "memberCount",
            expression = "java(entity.getMembers() != null ? entity.getMembers().size() : 0)"
    )
    GroupChatDTO toDto(GroupChat entity);
}
