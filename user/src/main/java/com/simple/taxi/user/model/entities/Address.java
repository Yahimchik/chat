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
@Table("addresses")
public class Address {

    @Id
    private UUID id;

    @Column("country")
    private String country;

    @Column("city")
    private String city;

    @Column("street")
    private String street;

    @Column("building_number")
    private String buildingNumber;

    @Column("entrance")
    private String entrance;

    @Column("floor")
    private String floor;

    @Column("apartment_number")
    private String apartmentNumber;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("deleted_at")
    private Instant deletedAt;
}
