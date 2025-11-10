package com.simple.taxi.user.model.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("user_status")
public class UserStatus {

    @Id
    private UUID userId; // совпадает с userId из UserProfile

    @Column("is_online")
    private Boolean isOnline;

    @Column("last_seen_at")
    private Instant lastSeenAt;
}
