package com.simple.taxi.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.taxi.user.model.entities.OutboxEvent;
import com.simple.taxi.user.model.enums.AggregateType;
import com.simple.taxi.user.repository.OutboxEventRepository;
import com.simple.taxi.user.service.OutboxService;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> publishEvent(AggregateType aggregateType, UUID aggregateId, String eventType, Object payload) {
        try {
            Json json = Json.of(objectMapper.writeValueAsString(payload));
            OutboxEvent event = OutboxEvent.builder()
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .type(eventType)
                    .payload(json)
                    .createdAt(Instant.now())
                    .status("NEW")
                    .build();
            return repository.save(event).then();
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}