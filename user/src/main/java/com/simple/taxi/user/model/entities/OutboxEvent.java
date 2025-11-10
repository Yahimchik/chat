package com.simple.taxi.user.model.entities;

import com.simple.taxi.user.model.enums.AggregateType;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("outbox_event")
public class OutboxEvent {
    @Id
    private UUID id;
    private AggregateType aggregateType;
    private UUID aggregateId;
    private String type;
    private Json payload;
    private Instant createdAt;
    private Instant processedAt;
    private String status;
}
