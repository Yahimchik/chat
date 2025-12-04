package org.chat.chat_service.service;

import org.chat.chat_service.model.dto.GroupChatCreateRequest;
import org.chat.chat_service.model.dto.GroupChatDTO;
import org.chat.chat_service.model.dto.GroupChatDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GroupChatService {

    Page<GroupChatDTO> getAllGroups(Pageable pageable);

    GroupChatDetailsDTO getGroupById(UUID groupId);

    GroupChatDetailsDTO createGroup(GroupChatCreateRequest request, UUID creatorId);

    void deleteGroup(UUID groupId, UUID currentUserId);

    void joinGroup(UUID groupId, UUID userId);

    void leaveGroup(UUID groupId, UUID userId);
}
