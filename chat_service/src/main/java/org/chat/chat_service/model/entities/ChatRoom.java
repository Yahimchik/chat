package org.chat.chat_service.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chat.chat_service.model.enums.ChatRoomType;
import org.chat.chat_service.model.enums.ChatType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "chat_rooms")
@NamedEntityGraph(name = "ChatRoom.withMembers", attributeNodes = @NamedAttributeNode("members"))
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType type;

    @OneToOne(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private GroupChat groupChat;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChatRoomMember> members = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public void addMember(ChatRoomMember member) {
        members.add(member);
        member.setRoom(this);
    }

    public Set<UUID> getParticipantIds() {
        return members.stream()
                .map(ChatRoomMember::getUserId)
                .collect(Collectors.toSet());
    }
}
