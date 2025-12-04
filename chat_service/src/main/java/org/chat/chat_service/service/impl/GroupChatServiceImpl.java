package org.chat.chat_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chat_service.mapper.GroupChatMapper;
import org.chat.chat_service.model.dto.GroupChatCreateRequest;
import org.chat.chat_service.model.dto.GroupChatDTO;
import org.chat.chat_service.model.dto.GroupChatDetailsDTO;
import org.chat.chat_service.model.entities.ChatRoom;
import org.chat.chat_service.model.entities.ChatRoomMember;
import org.chat.chat_service.model.entities.GroupChat;
import org.chat.chat_service.model.entities.GroupChatMember;
import org.chat.chat_service.model.enums.ChatRoomType;
import org.chat.chat_service.model.enums.ChatType;
import org.chat.chat_service.model.enums.Role;
import org.chat.chat_service.repository.GroupChatMemberRepository;
import org.chat.chat_service.repository.GroupChatRepository;
import org.chat.chat_service.service.GroupChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupChatServiceImpl implements GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final GroupChatMemberRepository groupChatMemberRepository;
    private final GroupChatMapper groupChatMapper;

    @Override
    public Page<GroupChatDTO> getAllGroups(Pageable pageable) {
        return groupChatRepository.findAll(pageable).map(groupChatMapper::toDto);
    }

    @Override
    public GroupChatDetailsDTO getGroupById(UUID groupId) {
        return groupChatRepository.findById(groupId)
                .map(groupChatMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + groupId + " not found"));
    }

    @Override
    @Transactional
    public GroupChatDetailsDTO createGroup(GroupChatCreateRequest request, UUID creatorId) {
        GroupChat groupChat = groupChatMapper.toEntity(request);
        groupChat.setCreatedByUserId(creatorId);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setType(ChatRoomType.GROUP);
        groupChat.setChatRoom(chatRoom);

        addMemberToGroup(groupChat, creatorId, Role.OWNER);

        GroupChat savedGroup = groupChatRepository.save(groupChat);

        return groupChatMapper.toDetailsDto(savedGroup);
    }

    @Override
    @Transactional
    public void deleteGroup(UUID groupId, UUID currentUserId) {
        GroupChat groupChat = findGroupByIdOrThrow(groupId);
        if (!groupChat.getCreatedByUserId().equals(currentUserId)) {
            throw new RuntimeException("Only the owner can delete the group.");
        }
        groupChatRepository.delete(groupChat);
    }

    @Override
    @Transactional
    public void joinGroup(UUID groupId, UUID userId) {
        GroupChat groupChat = findGroupByIdOrThrow(groupId);

        if (groupChatMemberRepository.existsByGroupChatIdAndUserId(groupId, userId)) {
            return;
        }

        if (groupChat.getType() == ChatType.PRIVATE) {
            throw new RuntimeException("Cannot join a private group directly.");
        }

        addMemberToGroup(groupChat, userId, Role.MEMBER);
        groupChatRepository.save(groupChat);
    }

    @Override
    public void leaveGroup(UUID groupId, UUID userId) {
        GroupChatMember member = groupChatMemberRepository.findByGroupChatIdAndUserId(groupId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found in the group."));

        if (member.getRole() == Role.OWNER) {
            throw new IllegalStateException("Owner cannot leave the group. Delete it or transfer ownership.");
        }

        GroupChat groupChat = member.getGroupChat();
        groupChat.getMembers().remove(member);
        groupChat.getChatRoom().getMembers().removeIf(m -> m.getUserId().equals(userId));

        groupChatRepository.save(groupChat);
    }

    private GroupChat findGroupByIdOrThrow(UUID groupId) {
        return groupChatRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + groupId + " not found"));
    }

    private void addMemberToGroup(GroupChat group, UUID userId, Role role) {
        GroupChatMember groupMember = new GroupChatMember();
        groupMember.setGroupChat(group);
        groupMember.setUserId(userId);
        groupMember.setRole(role);
        group.addMember(groupMember);

        ChatRoomMember roomMember = new ChatRoomMember();
        roomMember.setRoom(group.getChatRoom());
        roomMember.setUserId(userId);
        group.getChatRoom().addMember(roomMember);
    }
}