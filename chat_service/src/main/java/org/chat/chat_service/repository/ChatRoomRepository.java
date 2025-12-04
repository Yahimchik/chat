package org.chat.chat_service.repository;

import org.chat.chat_service.model.entities.ChatRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    @Query("""
            SELECT  cr FROM ChatRoom cr
            WHERE cr.type = 'PRIVATE'
            AND EXISTS (SELECT 1 FROM ChatRoomMember crm WHERE crm.room = cr AND crm.userId = :user1Id)
            AND EXISTS (SELECT 1 FROM ChatRoomMember crm WHERE crm.room = cr AND crm.userId = :user2Id)
            """)
    Optional<ChatRoom> findPrivateRoomBetweenUsers(@Param("user1Id") UUID user1Id, @Param("user2Id") UUID user2Id);

    @EntityGraph(value = "ChatRoom.withMembers", type = EntityGraph.EntityGraphType.LOAD)
    Optional<ChatRoom> findWithMembersById(UUID roomId);
}