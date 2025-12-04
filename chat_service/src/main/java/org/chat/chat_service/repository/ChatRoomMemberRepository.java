package org.chat.chat_service.repository;

import org.chat.chat_service.model.entities.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, UUID> {

    boolean existsByRoomIdAndUserId(UUID roomId, UUID userId);
}