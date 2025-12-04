package org.chat.chat_service.repository;

import org.chat.chat_service.model.entities.GroupChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, UUID>, JpaSpecificationExecutor<GroupChat> {

    Page<GroupChat> findByMembers_UserId(UUID userId, Pageable pageable);
}
