package org.chat.chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.chat.chat_service.model.entities.ChatRoom;
import org.chat.chat_service.repository.ChatRoomRepository;
import org.chat.chat_service.service.ChatRoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional(readOnly = true)
    public Set<UUID> getRoomParticipants(UUID roomId) {
        return chatRoomRepository.findById(roomId)
                .map(ChatRoom::getParticipantIds)
                .orElse(Collections.emptySet());
    }
}

