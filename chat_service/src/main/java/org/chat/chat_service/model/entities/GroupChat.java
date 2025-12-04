package org.chat.chat_service.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chat.chat_service.model.enums.ChatType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "group_chats")
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_url")
    private String coverUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType type;

    @Column(name = "created_by_user_id", nullable = false)
    private UUID createdByUserId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "groupChat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GroupChatMember> members = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private ChatRoom chatRoom;

    public void addMember(GroupChatMember member) {
        members.add(member);
        member.setGroupChat(this);
    }
}
