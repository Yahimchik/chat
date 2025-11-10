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
@Table("apartments")
public class Apartment {
    @Id
    private UUID id;

    @Column("address_id")
    private UUID addressId;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("deleted_at")
    private Instant deletedAt;
}
