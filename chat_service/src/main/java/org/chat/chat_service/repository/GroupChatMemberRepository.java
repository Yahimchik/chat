package org.chat.chat_service.repository;

import org.chat.chat_service.model.entities.GroupChatMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupChatMemberRepository extends JpaRepository<GroupChatMember, UUID> {

    Optional<GroupChatMember> findByGroupChatIdAndUserId(UUID groupChatId, UUID userId);

    boolean existsByGroupChatIdAndUserId(UUID groupChatId, UUID userId);

    Page<GroupChatMember> findByGroupChatId(UUID groupChatId, Pageable pageable);
}
