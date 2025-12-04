package org.chat.chat_service.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chat.chat_service.model.enums.Role;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "group_chat_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"group_chat_id", "user_id"})
})
public class GroupChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_chat_id", nullable = false)
    private GroupChat groupChat;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "is_club_member", nullable = false)
    private boolean isClubMember = false;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;
}

