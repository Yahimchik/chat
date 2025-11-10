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
@Table("user_apartments")
public class UserApartment {

    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId; // ссылка на UserProfile

    @Column("apartment_id")
    private UUID apartmentId; // ссылка на Apartment Service

    @Column("joined_at")
    private Instant joinedAt;

    @Column("is_active")
    private Boolean isActive; // активное членство
}
