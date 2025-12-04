package org.chat.chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chat.chat_service.client.UserProfileClient;
import org.chat.chat_service.mapper.ChatMessageMapper;
import org.chat.chat_service.model.dto.ChatMessageDTO;
import org.chat.chat_service.model.dto.MessageSendRequest;
import org.chat.chat_service.model.dto.UserProfileDTO;
import org.chat.chat_service.model.entities.ChatMessage;
import org.chat.chat_service.model.entities.ChatRoom;
import org.chat.chat_service.model.entities.ChatRoomMember;
import org.chat.chat_service.model.enums.ChatRoomType;
import org.chat.chat_service.repository.ChatMessageRepository;
import org.chat.chat_service.repository.ChatRoomMemberRepository;
import org.chat.chat_service.repository.ChatRoomRepository;
import org.chat.chat_service.service.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final UserProfileClient userProfileClient;

    @Override
    @Transactional
    public ChatMessageDTO sendMessage(MessageSendRequest request, UUID senderId) {
        UUID roomId = request.getRoomId();

        if (!chatRoomMemberRepository.existsByRoomIdAndUserId(roomId, senderId)) {
            throw new RuntimeException("User " + senderId + " is not a member of room " + roomId);
        }

        ChatRoom room = chatRoomRepository.getReferenceById(roomId);
        ChatMessage message = new ChatMessage();
        message.setRoom(room);
        message.setSenderId(senderId);
        message.setContent(request.getContent());

        ChatMessage savedMessage = chatMessageRepository.save(message);
        log.info("Message from user {} saved to room {}", senderId, roomId);

        ChatMessageDTO dto = chatMessageMapper.toDto(savedMessage);

        enrichMessageDtos(List.of(dto));


        return dto;
    }

    @Override
    public Page<ChatMessageDTO> getMessagesForRoom(UUID roomId, UUID currentUserId, Pageable pageable) {
        if (!chatRoomMemberRepository.existsByRoomIdAndUserId(roomId, currentUserId)) {
            throw new RuntimeException("User " + currentUserId + " does not have access to room " + roomId);
        }

        Page<ChatMessage> messagePage = chatMessageRepository.findByRoomId(roomId, pageable);

        Page<ChatMessageDTO> dtoPage = messagePage.map(chatMessageMapper::toDto);

        enrichMessageDtos(dtoPage.getContent());

        return dtoPage;
    }

    @Override
    public UUID findOrCreatePrivateRoom(UUID userFirstId, UUID userSecondId) {
        if (userFirstId.equals(userSecondId)) {
            throw new IllegalArgumentException("Cannot create a private chat with yourself.");
        }


        return chatRoomRepository.findPrivateRoomBetweenUsers(userFirstId, userSecondId)
                .map(ChatRoom::getId)
                .orElseGet(() -> createPrivateRoom(userFirstId, userSecondId));
    }

    private UUID createPrivateRoom(UUID user1Id, UUID user2Id) {
        log.info("Creating a new private room for users {} and {}", user1Id, user2Id);
        ChatRoom room = new ChatRoom();
        room.setType(ChatRoomType.PRIVATE);

        ChatRoomMember member1 = new ChatRoomMember();
        member1.setUserId(user1Id);
        room.addMember(member1);

        ChatRoomMember member2 = new ChatRoomMember();
        member2.setUserId(user2Id);
        room.addMember(member2);

        return chatRoomRepository.save(room).getId();
    }

    /**
     * Эффективно обогащает список DTO сообщений данными об отправителях (имя, аватар).
     *
     * @param messages Список DTO для обогащения.
     */
    private void enrichMessageDtos(List<ChatMessageDTO> messages) {
        if (messages.isEmpty()) {
            return;
        }

        Set<UUID> senderIds = messages.stream()
                .map(ChatMessageDTO::getSenderId)
                .collect(Collectors.toSet());

        Map<UUID, UserProfileDTO> profiles = userProfileClient.getProfilesMap(senderIds);

        messages.forEach(dto -> {
            UserProfileDTO details = profiles.getOrDefault(
                    dto.getSenderId(),
                    UserProfileDTO.unknown(dto.getSenderId())
            );

            dto.setSenderName(details.firstName());
            dto.setSenderAvatarUrl(details.avatarUrl());
        });

    }
}
